package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.MiscUtils;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;

public class NextItemCommand implements BlockHuntCommand {
    @Override
    public String getCommandName() {
        return "next";
    }

    @Override
    public void runCommand(Player player, String[] args) {
        if (ScanBlockHunt.runningGame == null) {
            Messenger.sendMessage(player, "You need to start an actual game before you can start a new round!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (!ScanBlockHunt.runningGame.hasPermissions(player)) {
            Messenger.sendMessage(player, "You must be a host or admin to start a new round!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (args.length <= 1) {
            Messenger.sendMessage(player, "You need to specify an actual item for the next round or say \"random\" for a random item!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        } else if (ScanBlockHunt.roundGoing) {
            Messenger.sendMessage(player, "You need to end the current round in order to start a new one! (use /sbh endround)", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return;
        }
        if (ScanBlockHunt.runningGame.getGameHost().equals(player)) {
            if (args[1].equalsIgnoreCase("random")) {
                Messenger.sendMessage(player, "Choosing a random item for the next round!", MessageSeverity.INFO);
                Random rand = new Random();
                Material[] materials = Material.values();
                ScanBlockHunt.runningGame.setItem(materials[rand.nextInt(materials.length)]);
            } else {
                Material item = Material.matchMaterial(args[1]);
                if (item == null) {
                    Messenger.sendMessage(player, "That is not a valid item!", MessageSeverity.INCORRECT_COMMAND_USAGE);
                } else {
                    Messenger.sendMessage(player, "Next item is " + MiscUtils.getDisplayName(item) + "!", MessageSeverity.INFO);
                    ScanBlockHunt.runningGame.setItem(item);
                }
            }
        } else {
            Messenger.sendMessage(player, "You need to be the game host to choose the next item!", MessageSeverity.INCORRECT_COMMAND_USAGE);
        }
    }
}
