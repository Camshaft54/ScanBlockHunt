package io.github.MetalTurtle18.ScanBlockHunt.commands;

import io.github.MetalTurtle18.ScanBlockHunt.ScanBlockHunt;
import io.github.MetalTurtle18.ScanBlockHunt.util.BlockHuntCommand;
import io.github.MetalTurtle18.ScanBlockHunt.util.Messenger;
import io.github.MetalTurtle18.ScanBlockHunt.util.enums.MessageSeverity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class JoinGameCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "join";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        Player joiningPlayer;
        if (args.length >= 2) {
            joiningPlayer = Bukkit.getPlayer(args[1]);
            if (joiningPlayer == null) {
                Messenger.sendMessage(player, "The player " + args[1] + " does not exist or is not online!", MessageSeverity.INCORRECT_COMMAND_USAGE);
                return;
            }
        } else {
            joiningPlayer = player;
        }
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "There is no game running!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        if (ScanBlockHunt.runningGame.getPlayers().contains(joiningPlayer)) {
            Messenger.sendMessage(player, "The player " + joiningPlayer.getDisplayName() + " is already in the game!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        // If nothing else caught it, the player can be added to the game
        ScanBlockHunt.runningGame.addPlayer(joiningPlayer);
        Messenger.sendMessage(player, "Successfully added " + joiningPlayer.getDisplayName() + " to the game!", MessageSeverity.INFO);
    }
}
