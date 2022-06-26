package me.maanraj514.contrapvp.game.state.substates;

import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameState;
import me.maanraj514.contrapvp.game.state.GameStateListener;

public class NoneGameState extends GameState {

    @Override
    public void onEnable(ContraPVP plugin) {
    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return null;
    }
}
