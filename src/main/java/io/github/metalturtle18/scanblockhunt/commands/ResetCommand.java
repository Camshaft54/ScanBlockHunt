package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.entity.Player;

public class ResetCommand implements BlockHuntCommand {

    @Override
    public String getCommandName() {
        return "reset";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "A game must be running in order to reset it!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.hasPermissions(player)) {
            Messenger.sendMessage(player, "You must be a server admin or host of the game to run this command!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        ScanBlockHunt.runningGame.reset();
        Messenger.sendMessage(player, "Reset all scores and cleared current round!", MessageSeverity.INFO);
    }
}
