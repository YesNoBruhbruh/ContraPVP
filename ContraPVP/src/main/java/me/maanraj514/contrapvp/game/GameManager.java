package me.maanraj514.contrapvp.game;

import me.maanraj514.contrapvp.ContraPVP;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {
    private final ContraPVP plugin;

    private final Map<UUID, Game> games;

    public GameManager(ContraPVP plugin){
        this.plugin = plugin;
        games = new HashMap<>();
    }

    public void createGame(GameMode mode, String map){
        UUID uuid = UUID.randomUUID();
        this.games.put(uuid, new Game(uuid, mode, map, plugin));
        plugin.getLogger().info("Created game for map: " + map);
    }

    public void deleteGame(UUID uuid){
        this.games.get(uuid).delete();
        this.games.remove(uuid);
    }

    public Game getGame(UUID uuid){
        return games.get(uuid);
    }
}