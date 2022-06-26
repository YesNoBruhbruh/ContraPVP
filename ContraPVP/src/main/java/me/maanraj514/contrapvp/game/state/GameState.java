package me.maanraj514.contrapvp.game.state;

import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public abstract class GameState {

    protected Game game;
    private GameStateListener listener;

    public void pre(Game game, ContraPVP plugin){
        this.game = game;
        GameStateListener listener1 = getListener(game);
        if(listener1 != null){
            Bukkit.getPluginManager().registerEvents(listener1, plugin);
            this.listener = listener1;
        }
        this.onEnable(plugin);
    }

    public void post(Game game){
        this.game = game;
        if(listener != null){
            HandlerList.unregisterAll(listener);
        }
        this.onDisable();
    }

    public abstract void onEnable(ContraPVP plugin);

    abstract public void onDisable();
    abstract public GameStateListener getListener(Game game);

}
