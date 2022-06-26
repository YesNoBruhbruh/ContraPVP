package me.maanraj514.contrapvp.commands;

import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "setup", args = {"map"}, permission = "ContraPVP.setup", playerOnly = true)
public class SetupCommand extends Command {
    private final ContraPVP plugin;

    public SetupCommand(ContraPVP plugin){
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;

        if(!plugin.getWizardManager().hasSession(player.getUniqueId())){
            plugin.getWizardManager().createSession(player, args[0]);
        } else {
            player.sendMessage(MessageUtils.format("You already have a running session."));
        }
    }
}
