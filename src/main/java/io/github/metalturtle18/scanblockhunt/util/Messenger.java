package io.github.metalturtle18.scanblockhunt.util;

import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Messenger {
    // Overload so you can just send to a player if there is a player argument otherwise send to console
    public static void sendMessage(String message, MessageSeverity severity) {
        Bukkit.getConsoleSender().sendMessage(severity.getPrefix() + message);
    }

    public static void sendMessage(Player player, String message, MessageSeverity severity) {
        player.sendMessage(severity.getPrefix() + message);
    }

    public static void sendTitle(String title, String subtitle) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(title, subtitle, 10, 70, 20);
        }
    }
}
