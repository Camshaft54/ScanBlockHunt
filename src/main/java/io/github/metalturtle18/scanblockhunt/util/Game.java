package io.github.metalturtle18.scanblockhunt.util;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

public class Game {
    @Getter private final Player gameHost;
    @Getter private final HashMap<Player, Score> players = new HashMap<>();
    @Getter private Material currentItem;
    private int roundNumber;
    private Scoreboard scoreboard;
    private Objective itemObj;

    public Game(Player host) {
        currentItem = null;
        roundNumber = 0;
        if (Bukkit.getScoreboardManager() != null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            itemObj = scoreboard.registerNewObjective("item", "", "N/A Round 0");
            itemObj.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setScoreboard(scoreboard);
            }
        }
        // TODO: Disable plugin if manager is null
        gameHost = host;
        addPlayer(host);
    }

    public void addPlayer(Player player) {
        scoreboard.registerNewTeam(player.getDisplayName());
        players.put(player, itemObj.getScore(player.getDisplayName()));
        itemObj.getScore(player.getDisplayName()).setScore(0);
    }

    public void setItem(Material item) {
        currentItem = item;
        ScanBlockHunt.roundGoing = true;
        roundNumber++;
        itemObj.setDisplayName(currentItem.name() + " Round " + roundNumber);
    }

    public void itemFound(Player foundByPlayer) {
        ScanBlockHunt.roundGoing = false;
        Messenger.sendTitle(foundByPlayer.getDisplayName() + " found a(n) " + currentItem + "!", "");
        players.get(foundByPlayer).setScore(players.get(foundByPlayer).getScore() + 1);
    }
}
