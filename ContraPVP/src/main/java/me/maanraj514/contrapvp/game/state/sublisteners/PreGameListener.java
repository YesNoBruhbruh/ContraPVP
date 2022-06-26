package me.maanraj514.contrapvp.game.state.sublisteners;

import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameStateListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PreGameListener extends GameStateListener {

    private final Game game;

    public PreGameListener(Game game){
        this.game = game;
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if(game.getPlayers().containsKey(event.getWhoClicked().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onArrowShoot(ProjectileLaunchEvent event) {
        if(game.getPlayers().containsKey(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onItemPickup(EntityPickupItemEvent event){
        if (!(event.getEntity() instanceof Player)) return;

        if(game.getPlayers().containsKey(event.getEntity().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event){
        if(game.getPlayers().containsKey(event.getEntity().getUniqueId())){
            event.setCancelled(true);
        }
    }
}