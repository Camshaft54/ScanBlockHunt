package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class GameInfoCommand implements BlockHuntCommand {

    @Override
    public String getCommandName() {
        return "gameinfo";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "There is no game running!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        // TODO more checks and stuff
        Messenger.sendMessage(player, "Game currently running!\nHosted by " + ScanBlockHunt.runningGame.getGameHost(), MessageSeverity.INFO);
        String playerString = Arrays.toString(ScanBlockHunt.runningGame.getPlayers().keySet().toArray()).replaceAll("(\\[|\\])", "");
        Messenger.sendMessage(player, ScanBlockHunt.runningGame.getPlayers().size() + "Players: " + playerString, MessageSeverity.INFO);
    }
}
