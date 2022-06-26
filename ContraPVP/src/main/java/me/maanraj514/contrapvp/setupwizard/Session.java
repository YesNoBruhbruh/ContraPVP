package me.maanraj514.contrapvp.setupwizard;

import lombok.Getter;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.GameMode;
import me.maanraj514.contrapvp.object.MapData;
import me.maanraj514.contrapvp.object.Pos;
import me.maanraj514.contrapvp.setupwizard.gui.ConfirmExitGui;
import me.maanraj514.contrapvp.setupwizard.gui.WizardGui;
import me.maanraj514.contrapvp.utils.Hologram;
import me.maanraj514.contrapvp.utils.ItemBuilder;
import me.maanraj514.contrapvp.utils.MessageUtils;
import me.maanraj514.contrapvp.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Session {
    private final ContraPVP plugin;
    @Getter
    private final Player player;

    @Getter
    private final MapData data;

    private final List<Hologram> holograms;

    public Session(Player player, String mapName, ContraPVP plugin){
        this.plugin = plugin;
        this.player = player;
        this.holograms = new ArrayList<>();

        player.sendMessage(MessageUtils.format("Creating session for map " + mapName + ". pls wait..."));

        this.data = new MapData(mapName, /* TODO map authors */ Arrays.asList("YesNoBruhbruh"), System.currentTimeMillis(), new ArrayList<>(), new Pos(0, 0, 0, 0, 0));

        plugin.getPlayerRollBackManager().save(player);

        if(!WorldUtils.worldExists(mapName, plugin)){
            player.sendMessage(MessageUtils.format("&cMap " + mapName + " not found!"));
            plugin.getWizardManager().endSession(player, false);
            return;
        }
        WorldUtils.loadWorld(mapName, plugin);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.teleport(new Location(Bukkit.getWorld(mapName), 0, 200, 0));

            ItemStack guiItem = new ItemBuilder(Material.COMPASS).setDisplayName("&6Open Gui").build();
            ItemStack centerXZItem = new ItemBuilder(Material.ARROW).setDisplayName("&6Center X-Z").build();
            ItemStack centerYawPitchItem = new ItemBuilder(Material.ARROW).setDisplayName("&6Center Yaw-Pitch").build();
            ItemStack exitItem = new ItemBuilder(Material.BARRIER).setDisplayName("&cExit Wizard").build();

            player.getInventory().setItem(0, guiItem);
            player.getInventory().setItem(3, centerXZItem);
            player.getInventory().setItem(5, centerYawPitchItem);
            player.getInventory().setItem(8, exitItem);

            player.sendMessage(MessageUtils.format("Created session, you can now edit the positions."));
        }, 20);

    }

    public void onInteract(ItemStack item){
        switch (item.getType()){
            case COMPASS:
                WizardGui wizardGui = new WizardGui(this);
                wizardGui.open(player);
                break;
            case BARRIER:
                ConfirmExitGui confirmExitGui = new ConfirmExitGui(plugin);
                confirmExitGui.open(player);
                break;
            case ARROW:
                if(item.hasItemMeta()){
                    switch (ChatColor.stripColor(item.getItemMeta().getDisplayName())){
                        case "Center X-Z":
                            Location newLoc = player.getLocation();
                            newLoc.setX(newLoc.getBlockX() + 0.5);
                            newLoc.setZ(newLoc.getBlockZ() + 0.5);
                            player.teleport(newLoc);
                            break;
                        case "Center Yaw-Pitch":
                            // TODO because idk how yet
                            break;
                    }
                }
                break;
        }
    }

    public void delete(boolean save){
        plugin.getPlayerRollBackManager().restore(player);
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());

        for(Hologram hologram : holograms){
            hologram.destroy();
        }

        WorldUtils.unloadWorld(data.getName());

        if(save){
            data.setLastEdit(System.currentTimeMillis());
            plugin.getMapDB().saveMapData(data.getName(), data);
        }

        player.sendMessage(MessageUtils.format("Successfully ended session, " + (save ? "&aSaved Map" : "&cDidnt save map")));
    }

    public void addSpawnPoint(){
        if(data.getSpawnPoints().size() == GameMode.DUELS.getMaxPlayers()){
            player.sendMessage(MessageUtils.format("&cYou cant add more then " + GameMode.DUELS.getMaxPlayers() + " spawnpoints!"));
            return;
        }
        Location location = player.getLocation();
        this.data.getSpawnPoints().add(new Pos(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
        this.holograms.add(new Hologram(location, () -> "&6Spawnpoint Pos", 20, plugin));
        player.sendMessage(MessageUtils.format("Successfully added &6Spawnpoint"));
    }

    public void setSpectatorSpawn(){
        Location location = player.getLocation();
        this.data.setSpectatorSpawn((new Pos(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch())));
        this.holograms.add(new Hologram(location, () -> "&6Spectator Pos", 20, plugin));
        player.sendMessage(MessageUtils.format("Successfully set &6Spectator Spawn"));
    }
}
