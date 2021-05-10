package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetScoreCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "setscore";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "There must be a game running to change a player's score!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.hasPermissions(player)) {
            Messenger.sendMessage(player, "You must be the host of the game or a server admin to change your score!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (args.length != 3) {
            Messenger.sendMessage(player, "You need to specify a player and the score you want to set!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }

        // Check if player is valid
        Player specifiedPlayer = Bukkit.getPlayer(args[1]);
        if (specifiedPlayer == null ) {
            Messenger.sendMessage(player, "The player you specified does not exist or is not online!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.getPlayers().containsKey(specifiedPlayer)) {
            Messenger.sendMessage(player, "The player you specified is not in the game!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }

        // Check if score is valid
        int score;
        try {
            score = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            Messenger.sendMessage(player, "The score you specified is not a number!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }

        ScanBlockHunt.runningGame.setScore(player, score);
        Messenger.sendMessage(player, "Set " + player.getDisplayName() + "'s score to " + score + "!", MessageSeverity.INFO);
    }
}
