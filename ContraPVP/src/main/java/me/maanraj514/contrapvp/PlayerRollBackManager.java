package me.maanraj514.contrapvp;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRollBackManager {
    private final ContraPVP plugin;

    private final Map<UUID, Location> previousLocationMap;
    private final Map<UUID, GameMode> previousGameModeMap;
    private final Map<UUID, ItemStack[]> previousInventoryContents;
    private final Map<UUID, ItemStack[]> previousArmourContents;
    private final Map<UUID, Integer> previousHungerValue;
    private final Map<UUID, Integer> previousLevelMap;
    private final Map<UUID, Double> previousHealthMap;

    public PlayerRollBackManager(ContraPVP plugin){
        this.plugin = plugin;

        this.previousLocationMap = new HashMap<>();
        this.previousGameModeMap = new HashMap<>();
        this.previousInventoryContents = new HashMap<>();
        this.previousArmourContents = new HashMap<>();
        this.previousHungerValue = new HashMap<>();
        this.previousLevelMap = new HashMap<>();
        this.previousHealthMap = new HashMap<>();
    }

    public void save(Player player) {
        previousLocationMap.put(player.getUniqueId(), player.getLocation());
        previousGameModeMap.put(player.getUniqueId(), player.getGameMode());
        previousInventoryContents.put(player.getUniqueId(), player.getInventory().getContents());
        previousArmourContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
        previousHungerValue.put(player.getUniqueId(), player.getFoodLevel());
        previousLevelMap.put(player.getUniqueId(), player.getLevel());
        previousHealthMap.put(player.getUniqueId(), player.getHealth());
        player.getInventory().clear();
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        player.setFoodLevel(20);
    }

    public void restore(Player player) {
        player.getInventory().clear();

        ItemStack[] inventoryContent = previousInventoryContents.get(player.getUniqueId());
        if(inventoryContent != null){
            player.getInventory().setContents(inventoryContent);
        }

        ItemStack[] armorContents = previousArmourContents.get(player.getUniqueId());
        if(armorContents != null){
            player.getInventory().setArmorContents(armorContents);
        }

        GameMode previousGameMode = previousGameModeMap.get(player.getUniqueId());
        if (previousGameMode != null){
            player.setGameMode(previousGameMode);
        }

        Location previousLocation = previousLocationMap.get(player.getUniqueId());
        if (previousLocation != null) {
            player.teleportAsync(previousLocation);
        }
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        player.setFoodLevel(previousHungerValue.getOrDefault(player.getUniqueId(), 20));

        player.setLevel(previousLevelMap.getOrDefault(player.getUniqueId(), 0));

        player.setHealth(previousHealthMap.getOrDefault(player.getUniqueId(), 20.0));

        previousHungerValue.remove(player.getUniqueId());
        previousLocationMap.remove(player.getUniqueId());
        previousInventoryContents.remove(player.getUniqueId());
        previousArmourContents.remove(player.getUniqueId());
        previousGameModeMap.remove(player.getUniqueId());
        previousLevelMap.remove(player.getUniqueId());

        if (plugin == null) return;
        Bukkit.getServer().getScheduler().runTaskLater(plugin, () -> player.setFireTicks(0), 2);
    }
}
