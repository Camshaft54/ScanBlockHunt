package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.entity.Player;

public class RemovePlayerCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "removeplayer";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "A game must be running in order to remove players from it!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.hasPermissions(player)) {
            Messenger.sendMessage(player, "You can only remove players if you are the host of the game or an admin!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (args.length != 2) {
            Messenger.sendMessage(player, "You must specify a valid player!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (ScanBlockHunt.runningGame.getGameHost().getDisplayName().equalsIgnoreCase(args[1])) {
            Messenger.sendMessage(player, "You cannot remove yourself from the game! To end a game use /sbh endgame.", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        for (Player p : ScanBlockHunt.runningGame.getPlayers().keySet()) {
            if (p.getDisplayName().equalsIgnoreCase(args[1])) {
                ScanBlockHunt.runningGame.removePlayer(p);
                Messenger.sendMessage(player, "Removed " + p.getDisplayName() + " from the game!", MessageSeverity.INCORRECT_COMMAND_USAGE);
                return;
            }
        }
        Messenger.sendMessage(player, "The specified player is not in the game!", MessageSeverity.INCORRECT_COMMAND_USAGE);
    }
}
