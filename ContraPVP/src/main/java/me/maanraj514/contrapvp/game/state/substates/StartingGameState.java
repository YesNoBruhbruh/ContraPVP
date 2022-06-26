package me.maanraj514.contrapvp.game.state.substates;

import lombok.Getter;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameState;
import me.maanraj514.contrapvp.game.state.GameStateListener;
import me.maanraj514.contrapvp.game.state.GameStatus;
import me.maanraj514.contrapvp.game.state.sublisteners.PreGameListener;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class StartingGameState extends GameState {
    @Getter
    private int secondsLeft = 10;
    private BukkitTask task;

    @Override
    public void onEnable(ContraPVP plugin) {
        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            secondsLeft--;
            if(secondsLeft == 0){
                game.setGameState(GameStatus.ACTIVE);
            }
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        if(task != null){
            task.cancel();
            task = null;
        }
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}
