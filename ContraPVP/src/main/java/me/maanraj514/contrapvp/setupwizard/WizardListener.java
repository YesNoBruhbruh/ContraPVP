package me.maanraj514.contrapvp.setupwizard;

import me.maanraj514.contrapvp.ContraPVP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class WizardListener implements Listener {
    private final ContraPVP plugin;

    public WizardListener(ContraPVP plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getItem() == null) return;
        if(plugin.getWizardManager().hasSession(player.getUniqueId())){
            plugin.getWizardManager().getSession(player.getUniqueId()).onInteract(event.getItem());
        }
    }
}
