package me.maanraj514.contrapvp.party;

import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.object.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PartyListener implements Listener {
    private final ContraPVP plugin;

    public PartyListener(ContraPVP plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = plugin.getGamePlayer(player);
        if(gamePlayer.isInParty()){
            plugin.getPartyManager().getParty(gamePlayer.getPartyId()).rejoin(player);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = plugin.getGamePlayer(player);
        if(gamePlayer.isInParty()){
            plugin.getPartyManager().getParty(gamePlayer.getPartyId()).removePlayer(player, true);
        }
    }
}