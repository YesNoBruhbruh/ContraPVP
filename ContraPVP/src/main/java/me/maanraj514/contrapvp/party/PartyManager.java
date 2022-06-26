package me.maanraj514.contrapvp.party;

import me.maanraj514.contrapvp.ContraPVP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyManager {

    private final Map<UUID, Party> parties;

    public PartyManager(){
        this.parties = new HashMap<>();
    }

    public void createParty(Player player, ContraPVP plugin){
        UUID uuid = UUID.randomUUID();
        plugin.getGamePlayer(player).setPartyId(uuid);
        this.parties.put(uuid, new Party(uuid, player, plugin));
    }

    public Party getParty(UUID uuid){
        return parties.get(uuid);
    }
}
