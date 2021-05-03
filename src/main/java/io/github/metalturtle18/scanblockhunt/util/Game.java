package io.github.metalturtle18.scanblockhunt.util;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

public class Game {
    @Getter private final Player gameHost;
    @Getter private final HashMap<Player, Score> players = new HashMap<>();
    @Getter private Material currentItem;
    private int roundNumber;
    private int roundTime;
    private Team roundTimeText;
    private Scoreboard scoreboard;
    private Objective itemObj;

    public Game(Player host) {
        currentItem = null;
        roundNumber = 0;
        roundTime = -1;
        if (Bukkit.getScoreboardManager() != null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            itemObj = scoreboard.registerNewObjective("item", "", "N/A Round 0");
            roundTimeText = scoreboard.registerNewTeam("- secs");
            itemObj.getScore(roundTimeText.getDisplayName()).setScore(999);
            itemObj.setDisplaySlot(DisplaySlot.SIDEBAR);
            updateScoreboardForAllPlayers();
        }
        // TODO: Disable plugin if manager is null
        gameHost = host;
        addPlayer(host);
    }

    public void addPlayer(Player player) {
        scoreboard.registerNewTeam(player.getDisplayName());
        players.put(player, itemObj.getScore(player.getDisplayName()));
        itemObj.getScore(player.getDisplayName()).setScore(0);
        player.setScoreboard(scoreboard);
    }

    public void removePlayer(Player player) {
        Team playerTeam = scoreboard.getTeam(player.getDisplayName());
        if (playerTeam != null) {
            playerTeam.unregister();
            players.remove(player);
        }
    }

    public void resetScores() {
        for (Player p : players.keySet()) {
            players.get(p).setScore(0);
        }
    }

    public void adjustScore(Player player, int amount) {
        if (players.containsKey(player)) {
            players.get(player).setScore(players.get(player).getScore() + amount);
        }
    }

    public void setItem(Material item) {
        currentItem = item;
        ScanBlockHunt.roundGoing = true;
        roundNumber++;
        roundTime = 0;
        itemObj.setDisplayName(ChatColor.GOLD + currentItem.name() + ChatColor.AQUA + " Round " + roundNumber);
        Messenger.sendTitle("Next item: " + currentItem.name(), "Round " + roundNumber);
        roundTime = 0;
    }

    public void itemFound(Player foundByPlayer) {
        ScanBlockHunt.roundGoing = false;
        Messenger.sendTitle( ChatColor.RED + foundByPlayer.getDisplayName() + " found " + currentItem + "!", "");
        players.get(foundByPlayer).setScore(players.get(foundByPlayer).getScore() + 1);
    }

    public void endRound() {
        ScanBlockHunt.roundGoing = false;
        Messenger.sendTitle("Round skipped!", "No one found " + currentItem);
        roundTime = 0;
    }

    public void endGame() {
        ScanBlockHunt.roundGoing = false;
        if (Bukkit.getScoreboardManager() != null) {
            Scoreboard dummyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective dummyObj = dummyScoreboard.registerNewObjective("dummy", "", "");
            dummyObj.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (Player p : players.keySet()) {
                p.setScoreboard(dummyScoreboard);
            }
        }
    }

    public void increaseTimeElapsed() {
        if (ScanBlockHunt.roundGoing) {
            scoreboard.resetScores(roundTimeText.getDisplayName());
            roundTime++;
            if (("" + roundTime).length() + 5 > 15) {
                roundTimeText.setDisplayName(">1000000000 secs");
            } else {
                roundTimeText.setDisplayName(roundTime + " secs");
            }
            itemObj.getScore(roundTimeText.getDisplayName()).setScore(999);
        }
    }

    public void updateScoreboardForAllPlayers() {
        for (Player p : players.keySet()) {
            p.setScoreboard(scoreboard);
        }
    }
}
