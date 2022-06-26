package me.maanraj514.contrapvp.game.state.sublisteners;

import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameStateListener;
import me.maanraj514.contrapvp.game.state.GameStatus;
import me.maanraj514.contrapvp.game.state.substates.ActiveGameState;
import me.maanraj514.contrapvp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActiveGameListener extends GameStateListener {
    private final ContraPVP plugin;
    private final ActiveGameState state;
    private final Game game;

    private final Map<UUID, UUID> lastDamager;
    private final Map<UUID, BukkitTask> tasks;

    public ActiveGameListener(Game game, ActiveGameState state, ContraPVP plugin){
        this.plugin = plugin;
        this.state = state;
        this.game = game;
        this.lastDamager = new HashMap<>();
        this.tasks = new HashMap<>();
    }

    @EventHandler
    public void onDeath(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(event.getDamager() instanceof Player){
                Player damager = (Player) event.getDamager();
                // friendly-fire
                int playerTeam = game.getPlayers().get(player.getUniqueId());
                int damagerTeam = game.getPlayers().get(damager.getUniqueId());
                if(playerTeam == damagerTeam){
                    event.setCancelled(true);
                    return;
                }
                lastDamager.put(player.getUniqueId(), damager.getUniqueId());
                if(tasks.containsKey(player.getUniqueId())){
                    tasks.get(player.getUniqueId()).cancel();
                    tasks.remove(player.getUniqueId());
                }
                tasks.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    tasks.remove(player.getUniqueId());
                    lastDamager.remove(player.getUniqueId());
                }, 1200));
            }

            if(event.getDamage() >= player.getHealth()){
                if(event.getDamager() instanceof Player){
                    // Killed by a player
                    Player killer = (Player) event.getDamager();
                    MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas killed by &c" + killer.getDisplayName(), game.getPlayers());

                    killPlayer(player, killer);
                } else {
                    // Killed by a mob or something
                    MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas killed by &c" + event.getDamager().getCustomName(), game.getPlayers());

                    killPlayer(player, null);
                }
                // Drop items from inventory
                for(ItemStack item : player.getInventory().getContents()){
                    if(item != null && item.getType() != Material.AIR){
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                for(ItemStack item : player.getInventory().getArmorContents()){
                    if(item != null && item.getType() != Material.AIR){
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.getPlayer().getLocation().getY() < 20){
            // Player fell in void
            Player player = event.getPlayer();
            if(lastDamager.containsKey(event.getPlayer().getUniqueId())){
                // Player was hit into the void
                OfflinePlayer damager = Bukkit.getOfflinePlayer(lastDamager.get(player.getUniqueId()));
                MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas hit into the void by &c" + damager.getName(), game.getPlayers());

                killPlayer(player, damager);
            } else {
                // Player fell
                MessageUtils.broadcast("&c" + player.getDisplayName() + " &rfell into the void", game.getPlayers());

                killPlayer(player, null);
            }
        }
    }
    private void killPlayer(Player player, OfflinePlayer killer){
        if(killer != null && killer.isOnline()){
            Player onlineKiller = Bukkit.getPlayer(killer.getUniqueId());
            onlineKiller.playSound(onlineKiller.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
            plugin.getGamePlayer(onlineKiller).addKill();
        }

        int team = game.getPlayers().get(player.getUniqueId());

        state.getAliveTeams().get(team).remove(player.getUniqueId());
        if(state.getAliveTeams().get(team).size() == 0){
            state.getAliveTeams().remove(team);
        }

        game.addSpectator(player);
        state.addDeath();

        if(state.getAliveTeams().size() == 1){
            // There is a winner
            game.setWinnerTeam(state.getAliveTeams().keySet().stream().findFirst().orElse(0));
            game.setGameState(GameStatus.ENDED);
        }
    }

}
