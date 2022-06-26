package me.maanraj514.contrapvp.object;

import lombok.Getter;
import lombok.Setter;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class GamePlayer {

    @Getter
    private final UUID uuid;
    @Getter
    @Setter
    private UUID gameId;

    @Getter
    @Setter
    private UUID partyId;
    @Getter
    @Setter
    private UUID pendingInvite;

    @Getter
    private int kills = 0;

    public GamePlayer(Player player) {
        this.uuid = player.getUniqueId();
    }

    public boolean isInGame(){
        return gameId != null;
    }

    public boolean isInParty(){
        return partyId != null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void addKill(){
        this.kills++;
    }
}
