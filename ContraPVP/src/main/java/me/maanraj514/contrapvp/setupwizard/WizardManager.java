package me.maanraj514.contrapvp.setupwizard;

import me.maanraj514.contrapvp.ContraPVP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WizardManager {
    private final ContraPVP plugin;

    private final Map<UUID, Session> sessions;

    public WizardManager(ContraPVP plugin){
        this.plugin = plugin;
        this.sessions = new HashMap<>();
    }

    public void createSession(Player player, String map){
        this.sessions.put(player.getUniqueId(), new Session(player, map, plugin));
    }

    public void endSession(Player player, boolean save){
        this.sessions.get(player.getUniqueId()).delete(save);
        this.sessions.remove(player.getUniqueId());
    }

    public Session getSession(UUID uuid){
        return sessions.get(uuid);
    }

    public boolean hasSession(UUID uuid){
        return sessions.containsKey(uuid);
    }
}
