package me.maanraj514.contrapvp.utils;

import com.grinderwolf.swm.api.exceptions.*;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import lombok.experimental.UtilityClass;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.game.Game;
import me.maanraj514.contrapvp.game.state.GameStatus;
import org.bukkit.Bukkit;

import java.io.IOException;

@UtilityClass
public class WorldUtils {
    private int id = 0;

    public void loadGameWorld(Game game, ContraPVP plugin){
        final SlimeLoader loader = plugin.getSlime().getLoader("mysql");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                SlimeWorld world = plugin.getSlime().loadWorld(loader, game.getMap(), true, new SlimePropertyMap()).clone(game.getMap() + "-" + id);


                plugin.getSlime().generateWorld(world);

                id++;

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    game.setWorld(Bukkit.getWorld(world.getName()));
                    game.setGameState(GameStatus.LOADING);
                }, 40);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldInUseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void loadWorld(String name, ContraPVP plugin){
        final SlimeLoader loader = plugin.getSlime().getLoader("mysql");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                SlimeWorld world = plugin.getSlime().loadWorld(loader, name, true, new SlimePropertyMap());

                plugin.getSlime().generateWorld(world);

            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldInUseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void unloadWorld(String name){
        try {
            Bukkit.unloadWorld(name, false);
        } catch (Exception ignored){}
    }

    public boolean worldExists(String name, ContraPVP plugin){
        final SlimeLoader loader = plugin.getSlime().getLoader("mysql");
        try {
            return loader.worldExists(name);
        } catch (IOException e) {
            return false;
        }
    }

}