package io.github.metalturtle18.scanblockhunt.util;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
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
            players.remove(player);
            Scoreboard dummy = Bukkit.getScoreboardManager().getNewScoreboard();
            dummy.registerNewObjective("dummy", "dummy", "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(dummy);
        }
    }

    /**
     * Resets the scores for all players in the game.
     */
    public void reset() {
        players.forEach((p, s) -> s.setScore(0));
        ScanBlockHunt.roundGoing = false;
        scoreboard.resetScores(roundTimeText.getDisplayName());
        roundTime = 0;
        roundTimeText.setDisplayName("0 seconds");
        itemObj.getScore(roundTimeText.getDisplayName()).setScore(999);
        itemObj.setDisplayName(ChatColor.GOLD + "N/A " + ChatColor.AQUA + "Round 0");
    }

    /**
     * Sets the score of a player by a specified amount.
     * @param player the player to have their score modified
     * @param amount the score to set
     */
    public void setScore(Player player, int amount) {
        if (players.containsKey(player)) {
            players.get(player).setScore(amount);
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
        itemObj.setDisplayName(ChatColor.GOLD + MiscUtils.getDisplayName(currentItem) + ChatColor.AQUA + " Round " + roundNumber);
        Messenger.sendTitle(MiscUtils.getDisplayName(currentItem), "Round " + roundNumber);

        // Check that no players already have the item and if they do, end the round.
        ArrayList<Player> playersWithItem = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            for (ItemStack itemStack : p.getInventory()) {
                if (itemStack != null && itemStack.getType().equals(item)) {
                    playersWithItem.add(p);
                    break;
                }
            }
        }
        if (playersWithItem.size() > 0) {
            ScanBlockHunt.roundGoing = false;
            if (playersWithItem.size() == 1) {
                Messenger.sendTitle(ChatColor.RED + playersWithItem.get(0).getDisplayName() + " found " + currentItem + "!", "");
            } else {
                String playerString = Arrays.toString(playersWithItem.stream().map(Player::getDisplayName).toArray());
                playerString = playerString.substring(1, playerString.length() - 1);
                Messenger.sendTitle("Multiple players found " + currentItem + "!", playerString + " all found the item!");
            }
            playersWithItem.forEach(p -> players.get(p).setScore(players.get(p).getScore() + 1));
            scoreboard.resetScores(roundTimeText.getDisplayName());
            roundTime = 0;
            roundTimeText.setDisplayName("0 seconds");
            itemObj.getScore(roundTimeText.getDisplayName()).setScore(999);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Ends the current round and adds a point to the specified player's score.
     * @param foundByPlayer the player who found the item and will be rewarded a point
     */
    public void itemFound(Player foundByPlayer) {
        ScanBlockHunt.roundGoing = false;
        Messenger.sendTitle(ChatColor.RED + foundByPlayer.getDisplayName() + " found " + currentItem + "!", "");
        players.get(foundByPlayer).setScore(players.get(foundByPlayer).getScore() + 1);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
        }
    }

    /**
     * Ends the current round without declaring a winner.
     */
    public void endRound() {
        ScanBlockHunt.roundGoing = false;
        roundTime = 0;
        Messenger.sendTitle("Round skipped!", ChatColor.RED + "" + ChatColor.STRIKETHROUGH + MiscUtils.getDisplayName(currentItem));
    }

    /**
     * Ends the entire game and removes all the players from it.
     */
    public void endGame() {
        itemObj.unregister();
        for (Player p : players.keySet()) {
            Team playerTeam = scoreboard.getTeam(p.getDisplayName());
            if (playerTeam != null) {
                playerTeam.unregister();
                players.remove(p);
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

    /**
     * Checks if the player specified has sufficient permissions to modify the game.
     * @param player the player to check permissions for
     * @return true if the player has permissions, false if they don't
     */
    public boolean hasPermissions(Player player) {
        return player.isOp() || player.equals(gameHost);
    }
}
