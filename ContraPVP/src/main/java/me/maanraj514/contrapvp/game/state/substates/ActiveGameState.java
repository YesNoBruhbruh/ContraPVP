package me.maanraj514.contrapvp.game.state.substates;

import lombok.Getter;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameState;
import me.maanraj514.contrapvp.game.state.GameStateListener;
import me.maanraj514.contrapvp.game.state.sublisteners.ActiveGameListener;

import java.util.*;

public class ActiveGameState extends GameState {
    private ContraPVP plugin;
    @Getter
    private Map<Integer, Set<UUID>> aliveTeams;

    @Getter
    private int playersAlive;

    @Override
    public void onEnable(ContraPVP plugin) {
        this.plugin = plugin;
        this.aliveTeams = new HashMap<>();
        this.playersAlive = game.getPlayers().size();
        for(int i = 1; i <= (game.getMode().getMaxPlayers() / game.getMode().getPlayersPerTeam()); i++){
            if(game.getTeams().get(i).size() != 0){
                this.aliveTeams.put(i, new HashSet<>());
                this.aliveTeams.put(i, game.getTeams().get(i));
            }
        }
    }

    public void addDeath(){
        this.playersAlive--;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return new ActiveGameListener(game, this, plugin);
    }
}
