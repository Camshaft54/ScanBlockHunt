package io.github.metalturtle18.scanblockhunt.util;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

/**
 * The Game class stores data about the current game, including the players, current item, and elapsed time for the
 * current round. It also contains methods for modifying the aforementioned values.
 * @author Camshaft54 and MetalTurtle18
 */
public class Game {
    @Getter
    private final Player gameHost;
    @Getter
    private final HashMap<Player, Score> players = new HashMap<>();
    @Getter
    private Material currentItem;
    private int roundNumber;
    private int roundTime;
    private Team roundTimeText;
    private Scoreboard scoreboard;
    private Objective itemObj;

    /**
     * Initializes the Game class and displays the scoreboard to all players currently in the game.
     * @param host the player who is hosting the game
     */
    public Game(Player host) {
        currentItem = null;
        roundNumber = 0;
        roundTime = 0;
        if (Bukkit.getScoreboardManager() != null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            itemObj = scoreboard.registerNewObjective("item", "", "N/A Round 0");
            roundTimeText = scoreboard.registerNewTeam("- secs");
            itemObj.getScore(roundTimeText.getDisplayName()).setScore(999);
            itemObj.setDisplaySlot(DisplaySlot.SIDEBAR);
            for (Player p : players.keySet()) {
                p.setScoreboard(scoreboard);
            }
        }
        // TODO: Disable plugin if manager is null
        gameHost = host;
        addPlayer(host);
    }

    /**
     * Adds a player to the game with a score of 0 and displays the game's scoreboard to them.
     * @param player the player to be added to the game
     */
    public void addPlayer(Player player) {
        scoreboard.registerNewTeam(player.getDisplayName());
        players.put(player, itemObj.getScore(player.getDisplayName()));
        itemObj.getScore(player.getDisplayName()).setScore(0);
        player.setScoreboard(scoreboard);
    }

    /**
     * Removes a player from the game and hides the game scoreboard from them.
     * @param player the player to be removed from the game
     */
    public void removePlayer(Player player) {
        Team playerTeam = scoreboard.getTeam(player.getDisplayName());
        if (playerTeam != null && Bukkit.getScoreboardManager() != null) {
            playerTeam.unregister();
            Scoreboard dummyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective dummyObj = dummyScoreboard.registerNewObjective("dummy", "", "");
            dummyObj.setDisplaySlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(dummyScoreboard);
            players.remove(player);
        }
    }

    /**
     * Resets the scores for all players in the game.
     */
    public void resetScores() {
        for (Player p : players.keySet()) {
            players.get(p).setScore(0);
        }
    }

    /**
     * Increases the score of a player by a specified amount.
     * @param player the player to have their score modified
     * @param amount the number of points to add or, if negative, subtract from the player
     */
    public void increaseScore(Player player, int amount) {
        if (players.containsKey(player)) {
            players.get(player).setScore(players.get(player).getScore() + amount);
        }
    }

    /**
     * Sets the next rounds item and starts the new round.
     * @param item the material that players will search for in the next round
     */
    public void setItem(Material item) {
        currentItem = item;
        ScanBlockHunt.roundGoing = true;
        roundNumber++;
        roundTime = 0;
        itemObj.setDisplayName(ChatColor.GOLD + currentItem.name() + ChatColor.AQUA + " Round " + roundNumber);
        Messenger.sendTitle("Next item: " + currentItem.name(), "Round " + roundNumber);
        roundTime = 0;
    }

    /**
     * Ends the current round and adds a point to the specified player's score.
     * @param foundByPlayer the player who found the item and will be rewarded a point
     */
    public void itemFound(Player foundByPlayer) {
        ScanBlockHunt.roundGoing = false;
        Messenger.sendTitle(ChatColor.RED + foundByPlayer.getDisplayName() + " found " + currentItem + "!", "");
        players.get(foundByPlayer).setScore(players.get(foundByPlayer).getScore() + 1);
    }

    /**
     * Ends the current round without declaring a winner.
     */
    public void endRound() {
        ScanBlockHunt.roundGoing = false;
        roundTime = 0;
        Messenger.sendTitle("Round skipped!", "No one found " + currentItem);
    }

    /**
     * Ends the entire game and removes all the players from it.
     */
    public void endGame() {
        if (Bukkit.getScoreboardManager() != null) {
            for (Player p : players.keySet()) {
                removePlayer(p);
            }
        }
        ScanBlockHunt.roundGoing = false;
        ScanBlockHunt.runningGame = null;
    }

    /**
     * Increase the time that has elapsed in the round by 1 second. This method also handles the string being too long
     * to display on the leaderboard.
     */
    public void increaseTimeElapsed() {
        if (ScanBlockHunt.roundGoing) {
            scoreboard.resetScores(roundTimeText.getDisplayName());
            roundTime++;
            if (("" + roundTime).length() > 7) {
                roundTimeText.setDisplayName(">1000000 seconds");
            } else {
                roundTimeText.setDisplayName(roundTime + " seconds");
            }
            itemObj.getScore(roundTimeText.getDisplayName()).setScore(999);
        }
    }
}
