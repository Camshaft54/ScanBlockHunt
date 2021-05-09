package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Set;

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
        Set<Player> players = ScanBlockHunt.runningGame.getPlayers().keySet();
        String playerString = Arrays.toString(players.stream().map(Player::getDisplayName).toArray());
        playerString = players.size() + ((players.size() != 1) ? " players: " : " player: ") + playerString.substring(1, playerString.length()-1);
        Messenger.sendMessage(player,
                "Game currently running!\n" +
                "Hosted by " + ScanBlockHunt.runningGame.getGameHost().getDisplayName() + "\n" +
                playerString + "\n" +
                "Active Round: " + ScanBlockHunt.roundGoing + "\n" +
                ((ScanBlockHunt.roundGoing) ? "Current Item: " + ScanBlockHunt.runningGame.getCurrentItem().name() : ""),
                MessageSeverity.INFO);
    }
}
