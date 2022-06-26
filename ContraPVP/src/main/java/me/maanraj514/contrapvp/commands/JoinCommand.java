package me.maanraj514.contrapvp.commands;

import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "join", args = {"map"}, permission = "ContraPVP.join", playerOnly = true)
public class JoinCommand extends Command {
    @Override
    public void onCommand(CommandSender commandSender, String[] strings) {
        Player player = (Player)commandSender;
    }
}
