package me.maanraj514.contrapvp.game.state.substates;

import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameState;
import me.maanraj514.contrapvp.game.state.GameStateListener;
import me.maanraj514.contrapvp.game.state.GameStatus;

public class LoadingGameState extends GameState {
    @Override
    public void onEnable(ContraPVP plugin) {

        //TODO loading stuff

        game.setGameState(GameStatus.WAITING);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return null;
    }
}
