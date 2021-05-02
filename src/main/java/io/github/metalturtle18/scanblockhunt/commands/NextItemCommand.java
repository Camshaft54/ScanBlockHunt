package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class NextItemCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "next";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (args.length <= 1) {
            Messenger.sendMessage(player, "You need to specify an item to get for the next round!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        if (ScanBlockHunt.runningGame.getGameHost().equals(player)) {
            Material item = Material.matchMaterial(args[1]);
            if (item == null) {
                Messenger.sendMessage(player, "That is not a valid item!", MessageSeverity.INCORRECT_COMMAND_USAGE);
                return;
            }
            ScanBlockHunt.runningGame.setItem(item);
        }
    }
}
