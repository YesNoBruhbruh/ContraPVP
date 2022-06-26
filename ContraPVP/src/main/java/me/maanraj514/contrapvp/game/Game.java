package me.maanraj514.contrapvp.game;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import lombok.Getter;
import lombok.Setter;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.state.GameState;
import me.maanraj514.contrapvp.game.state.GameStatus;
import me.maanraj514.contrapvp.game.state.substates.*;
import me.maanraj514.contrapvp.object.MapData;
import me.maanraj514.contrapvp.object.Pos;
import me.maanraj514.contrapvp.party.Party;
import me.maanraj514.contrapvp.utils.LocationUtils;
import me.maanraj514.contrapvp.utils.MessageUtils;
import me.maanraj514.contrapvp.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Game {
    private final ContraPVP plugin;

    @Getter
    private final String map;

    private final UUID uuid;

    @Getter
    private final GameMode mode;

    private final MapData data;

    @Getter
    private final Map<UUID, Integer> players;
    @Getter
    private final Map<Integer, Set<UUID>> teams;
    @Getter
    private final Set<UUID> spectators;

    private final JPerPlayerScoreboard scoreboard;

    @Getter
    @Setter
    private World world;
    private GameStatus status = GameStatus.NONE;
    private final Map<GameStatus, GameState> states;
    private final List<BukkitTask> tasks;

    @Getter
    @Setter
    private int winnerTeam = 0;

    public Game(UUID uuid, GameMode mode, String map, ContraPVP plugin) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.mode = mode;
        this.map = map;
        this.players = new HashMap<>();
        this.teams = new HashMap<>();
        this.spectators = new HashSet<>();
        this.states = new HashMap<>();
        this.tasks = new ArrayList<>();

        this.states.put(GameStatus.NONE, new NoneGameState());
        this.states.put(GameStatus.LOADING, new LoadingGameState());
        this.states.put(GameStatus.WAITING, new WaitingGameState());
        this.states.put(GameStatus.STARTING, new StartingGameState());
        this.states.put(GameStatus.ACTIVE, new ActiveGameState());
        this.states.put(GameStatus.ENDED, new EndedGameState());

        this.scoreboard = new JPerPlayerScoreboard((player) -> "&b&lContraPVP", this::getScoreboardLines);
        this.tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, scoreboard::updateScoreboard, 0, 20));

        this.data = plugin.getMapDB().loadMapData(map);

        WorldUtils.loadGameWorld(this, plugin);
    }

    public void addPlayer(Player player){
        if(status == GameStatus.LOADING){
            player.sendMessage(MessageUtils.format("That game is still loading"));
            Bukkit.getScheduler().runTaskLater(plugin, () -> this.addPlayer(player), 20);
            return;
        }
        if(players.size() == mode.getMaxPlayers()){
            this.addSpectator(player);
            return;
        }

        plugin.getPlayerRollBackManager().save(player);

        MessageUtils.broadcast("&a" + player.getDisplayName() + " has joined the match", players);

        for(int i = 1; i <= (mode.getMaxPlayers() / mode.getPlayersPerTeam()); i++){
            if(teams.containsKey(i)){
                if(teams.get(i).size() != mode.getPlayersPerTeam()){
                    teams.get(i).add(player.getUniqueId());
                    players.put(player.getUniqueId(), i);
                    break;
                }
            } else {
                teams.put(i, new HashSet<>());
                teams.get(i).add(player.getUniqueId());
                players.put(player.getUniqueId(), i);
                Bukkit.broadcastMessage(player.getDisplayName() + " Team set to: " + i);
                break;
            }
        }

        this.assignSpawnLocation(player);
        scoreboard.addPlayer(player);

        if(players.size() == mode.getMinPlayers()){
            this.setGameState(GameStatus.STARTING);
        }
    }

    public void addParty(Party party){
        if((players.size() + party.getOnlinePlayers().size()) >= mode.getMaxPlayers()){
            MessageUtils.sendMessageWithLines(Bukkit.getPlayer(party.getLeader()), "Party is to big for this game or game is full.");
            return;
        }

        int teamSize = mode.getPlayersPerTeam();

        List<UUID> partyPlayers = party.getOnlinePlayers();

        for(int i = 1; i <= (mode.getMaxPlayers() / mode.getPlayersPerTeam()); i++){
            if(teams.containsKey(i)){
                if((teams.get(i).size() + partyPlayers.size()) <= teamSize){
                    teams.get(i).addAll(partyPlayers);
                    final int team = i;
                    partyPlayers.forEach(uuid -> players.put(uuid, team));
                    partyPlayers.clear();
                    return;
                }
            } else {
                teams.put(i, new HashSet<>());
                if(partyPlayers.size() <= teamSize){
                    teams.get(i).addAll(partyPlayers);
                    final int team = i;
                    partyPlayers.forEach(uuid -> players.put(uuid, team));
                    partyPlayers.clear();
                    return;
                } else {
                    for(int i2 = 0; i <= teamSize; i++){
                        teams.get(i).add(partyPlayers.get(i2));
                        players.put(partyPlayers.get(i2), i);
                        partyPlayers.remove(i2);
                    }
                }
            }
        }

        partyPlayers.forEach(uuid -> this.addSpectator(Bukkit.getPlayer(uuid)));

        party.getOnlinePlayers().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            plugin.getPlayerRollBackManager().save(player);
            this.assignSpawnLocation(player);
            scoreboard.addPlayer(player);
            MessageUtils.broadcast("&a" + player.getDisplayName() + " has joined the match", players);
        });

    }

    public void removePlayer(Player player){
        int team = this.players.get(player.getUniqueId());
        this.players.remove(player.getUniqueId());
        this.teams.get(team).remove(player.getUniqueId());

        MessageUtils.broadcast("&c" + player.getDisplayName() + " has left the match", players);

        if(status == GameStatus.ACTIVE){
            ActiveGameState gameState = (ActiveGameState) states.get(status);
            gameState.getAliveTeams().get(team).remove(player.getUniqueId());
            if(gameState.getAliveTeams().size() == 1){
                this.winnerTeam = gameState.getAliveTeams().keySet().stream().findFirst().orElse(0);
                this.setGameState(GameStatus.ENDED);
                return;
            }
        }
        if(status == GameStatus.STARTING && players.size() < mode.getMinPlayers()){
            this.setGameState(GameStatus.WAITING);
        }

        // TODO spawn
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        plugin.getPlayerRollBackManager().restore(player);
    }

    public void addSpectator(Player player){
        this.spectators.add(player.getUniqueId());
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        plugin.getPlayerRollBackManager().save(player);
        player.teleport(LocationUtils.posToLocation(data.getSpectatorSpawn(), world));
    }

    public void removeSpectator(Player player){
        this.spectators.remove(player.getUniqueId());
        // TODO spawn
        player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);
        plugin.getPlayerRollBackManager().restore(player);
    }

    public void assignSpawnLocation(Player player){
        int team = players.get(player.getUniqueId());
        Pos pos = this.data.getSpawnPoints().get(team - 1);
        player.teleport(LocationUtils.posToLocation(pos, world));
    }

    public void delete(){
        this.scoreboard.destroy();
        this.tasks.forEach(BukkitTask::cancel);
    }

    public void setGameState(GameStatus status){
        if(this.status == status) return;
        this.states.get(this.status).post(this);
        this.status = status;
        this.states.get(this.status).pre(this, plugin);
    }

    public List<String> getScoreboardLines(Player player){
        List<String> lines = new ArrayList<>();
        lines.add("");
        switch (status){
            case WAITING:
                lines.add("Players: &6" + players.size() + "/" + mode.getMaxPlayers());
                lines.add("");
                lines.add("Waiting...");
                lines.add("");
                break;
            case STARTING:
                lines.add("Players: &6" + players.size() + "/" + mode.getMaxPlayers());
                lines.add("");
                lines.add("Starting in &6" + ((StartingGameState) states.get(status)).getSecondsLeft());
                lines.add("");
                break;
            case ACTIVE:
                lines.add("Players: &6" + ((ActiveGameState) states.get(status)).getPlayersAlive() + "/" + mode.getMaxPlayers());
                lines.add("");
                lines.add("Kills: &6" + plugin.getGamePlayer(player).getKills());
                lines.add("");
                if(teams.get(players.get(player.getUniqueId())).size() != 1){
                    lines.add("Team:");
                    Set<UUID> teammates = teams.get(players.get(player.getUniqueId()));
                    teammates.remove(player.getUniqueId());
                    for (UUID teammate : teammates) {
                        Player bTeammate = Bukkit.getPlayer(teammate);
                        // TODO check if dead or alive (change red or green)
                        lines.add("&6" + bTeammate.getName());
                    }
                    lines.add("");
                }
                break;
            case ENDED:
                // TODO winner names
                lines.add("Game ended");
                lines.add("Team " + winnerTeam + " won");
                lines.add("");
                break;
        }
        return lines;
    }
}
