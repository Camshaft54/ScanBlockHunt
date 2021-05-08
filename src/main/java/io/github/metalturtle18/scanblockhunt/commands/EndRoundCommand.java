package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.entity.Player;

public class EndRoundCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "endround";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (!ScanBlockHunt.roundGoing) {
            Messenger.sendMessage(player, "There must be a round going in order to skip it!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.getGameHost().equals(player)) {
            Messenger.sendMessage(player, "You must be the game's host in order to skip the current round!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        ScanBlockHunt.runningGame.endRound();
        Messenger.sendMessage(player,"Skipped the current round!", MessageSeverity.INFO);
    }
}
