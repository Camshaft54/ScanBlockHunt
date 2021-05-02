package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Game;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.entity.Player;

public class NewGameCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "create";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame != null) { // There is already a game running
            Messenger.sendMessage(player, "There is already a game running so you cannot start a new one!", MessageSeverity.INCORRECT_COMMAND_USAGE);
        } else {
            ScanBlockHunt.runningGame = new Game(player);
            Messenger.sendMessage(player, "Started a new game!", MessageSeverity.INFO);
        }
    }
}
