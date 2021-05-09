package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AddPlayerCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "addplayer";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "A game must be running in order to add players to it!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.getGameHost().equals(player)) {
            Messenger.sendMessage(player, "You can only add players if you are the host of the game!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (args.length != 2) {
            Messenger.sendMessage(player, "You must specify a valid player!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }

        Player playerToAdd = Bukkit.getPlayer(args[1]);
        if (playerToAdd != null) {
            if (ScanBlockHunt.runningGame.getPlayers().containsKey(playerToAdd)) {
                Messenger.sendMessage(player, "Player specified is already in the game!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            } else {
                ScanBlockHunt.runningGame.addPlayer(player);
                Messenger.sendMessage(player, "Successfully added " + playerToAdd.getDisplayName() + " to the game!", MessageSeverity.INFO);
            }
        } else {
            Messenger.sendMessage(player, "The player specified is not a valid, online player!", MessageSeverity.INCORRECT_COMMAND_USAGE);
        }
    }
}
