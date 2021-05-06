package io.github.metalturtle18.scanblockhunt.util;

import org.bukkit.entity.Player;

public interface BlockHuntCommand {
    /**
     * @return the name of the command to be used by the CommandManager
     */
    String getCommandName();

    /**
     * The method executed when a player runs it.
     * @param player the player who ran the command
     * @param args the args of the command
     */
    void runCommand(Player player, String[] args);
}
