package me.maanraj514.contrapvp.game.state.substates;

import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameState;
import me.maanraj514.contrapvp.game.state.GameStateListener;
import me.maanraj514.contrapvp.game.state.sublisteners.PreGameListener;

public class EndedGameState extends GameState {

    @Override
    public void onEnable(ContraPVP plugin) {

        //TODO ending stuff

    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}
