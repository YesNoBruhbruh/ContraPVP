package me.maanraj514.contrapvp.commands;

import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import me.maanraj514.contrapvp.ContraPVP;
import me.maanraj514.contrapvp.object.GamePlayer;
import me.maanraj514.contrapvp.party.Party;
import me.maanraj514.contrapvp.party.PartyRank;
import me.maanraj514.contrapvp.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandInfo(name = "party", permission = "ContraPVP.party", playerOnly = true)
public class PartyCommand extends Command {
    private final ContraPVP plugin;

    public PartyCommand(ContraPVP plugin){
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player)commandSender;

        GamePlayer gamePlayer = plugin.getGamePlayer(player);

        if (args.length == 0){
            player.sendMessage(MessageUtils.format("&cWrong usage: /party <create|invite|accept|leave|promote|demote|kick|transfer|kickoffline|disband> [player]"));
            return;
        }

        if (args.length == 1){
            switch (args[0]){
                case "create":
                    if (plugin.getPartyManager().getParty(gamePlayer.getPartyId()) != null){
                        player.sendMessage(MessageUtils.format("&cYou are already in a party!"));
                        return;
                    }
                    player.sendMessage(MessageUtils.format("&b&lCreating party..."));
                    plugin.getPartyManager().createParty(player, plugin);
                    break;
                case "accept":
                    //TODO make a check if the player that was invited is already in party
                    if(gamePlayer.getPendingInvite() != null){
                        plugin.getPartyManager().getParty(gamePlayer.getPendingInvite()).addPlayer(player);
                        return;
                    }
                    player.sendMessage(MessageUtils.format("&cYou have no pending invites!"));
                    break;
            }
        }

        if (args.length == 2){
            if (args[0].equalsIgnoreCase("invite")){
                Player player1 = Bukkit.getPlayer(args[1]);

                if (player1 == null){
                    player.sendMessage(MessageUtils.format("&cThat player doesnt exist"));
                    return;
                }

                GamePlayer gamePlayer1 = plugin.getGamePlayer(player1);

                if (gamePlayer1 == null){
                    player.sendMessage(MessageUtils.format("&cThat game player doesnt exist"));
                    return;
                }

                if (player.equals(player1)){
                    player.sendMessage(MessageUtils.format("&cYou can't invite yourself"));
                    return;
                }

                if (plugin.getPartyManager().getParty(gamePlayer.getPartyId()) == null){
                    player.sendMessage(MessageUtils.format("&cCreate a party first, do /party create"));
                    return;
                }

                if (plugin.getPartyManager().getParty(gamePlayer.getPartyId()).getPlayers().containsKey(player1.getUniqueId())){
                    player.sendMessage(MessageUtils.format("&cThat player is already in your party"));
                    return;
                }

                if (gamePlayer1.getPendingInvite() != null){
                    player.sendMessage(MessageUtils.format("&cThat player already has a pending invite"));
                    return;
                }
                player.sendMessage(MessageUtils.format("&b&lInviting player &b&l" + player1.getName()));
                plugin.getPartyManager().getParty(gamePlayer.getPartyId()).invite(player1, player);
            }
        }
    }
}