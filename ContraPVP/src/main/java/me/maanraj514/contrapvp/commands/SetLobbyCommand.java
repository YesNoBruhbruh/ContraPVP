package me.maanraj514.contrapvp.commands;

import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.utils.MessageUtils;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "setlobby", aliases = {"sl"}, permission = "ContraPVP.lobby", playerOnly = true)
public class SetLobbyCommand extends Command {
    private final ContraPVP plugin;

    public SetLobbyCommand(ContraPVP plugin){
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player)commandSender;

        double x, y, z, yaw, pitch;
        World world = player.getWorld();

        x = player.getLocation().getX();
        y = player.getLocation().getY();
        z = player.getLocation().getZ();
        yaw = player.getLocation().getYaw();
        pitch = player.getLocation().getPitch();

        plugin.getConfig().set("lobby-location.world", world.getName());
        plugin.getConfig().set("lobby-location.x", x);
        plugin.getConfig().set("lobby-location.y", y);
        plugin.getConfig().set("lobby-location.z", z);
        plugin.getConfig().set("lobby-location.yaw", yaw);
        plugin.getConfig().set("lobby-location.pitch", pitch);

        plugin.saveConfig();
        plugin.reloadConfig();

        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1f, 1f);
        player.sendMessage(MessageUtils.format("&aSuccessfully set the lobby location!"));
    }
}