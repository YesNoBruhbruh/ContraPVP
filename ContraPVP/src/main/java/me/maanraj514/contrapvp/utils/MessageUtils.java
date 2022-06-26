package me.maanraj514.contrapvp.utils;

import lombok.experimental.UtilityClass;
import me.maanraj514.contrapvp.object.GamePlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class MessageUtils {

    public String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String format(String message){
        return color("&b[&bContraPVP&b]&b " + message);
    }

    public void broadcast(String message, Player... players){
        for (Player player : players) {
            player.sendMessage(color(message));
        }
    }

    public void broadcast(String message, Map<UUID, Integer> players){
        for(UUID uuid : players.keySet()){
            Bukkit.getPlayer(uuid).sendMessage(color(message));
        }
    }

    public void broadcast(String message, List<GamePlayer> gamePlayers){
        for (GamePlayer gamePlayer : gamePlayers) {
            gamePlayer.getPlayer().sendMessage(color(message));
        }
    }

    public void broadcast(String message){
        Bukkit.broadcastMessage(color(message));
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut, Player... players){
        if(title != null && subTitle != null){
            for (Player player : players) {
                player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
            }
        }
    }

    public void sendActionbar(String message, Player... players){
        if(message != null){
            for (Player player : players) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(color(message)));
            }
        }
    }

    public void sendActionbar(String message, Player player){
        if(message != null){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(color(message)));
        }
    }

    public void sendMessageWithLines(Player player, String message){
        player.sendMessage(color("&6&m-------------------------"));
        player.sendMessage(color(message));
        player.sendMessage(color("&6&m-------------------------"));
    }

}
