package io.github.metalturtle18.scanblockhunt.events;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;
import java.util.List;

public class MoveItemEvent implements Listener {
    List<InventoryAction> validClickActions = Arrays.asList(InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.PLACE_ALL);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (ScanBlockHunt.roundGoing && ScanBlockHunt.runningGame.getPlayers().containsKey(player)) {
            if (event.isShiftClick() && event.getCurrentItem() != null &&
                    event.getCurrentItem().getType().equals(ScanBlockHunt.runningGame.getCurrentItem())) {
                ScanBlockHunt.runningGame.itemFound(player);
            } else if (validClickActions.contains(event.getAction()) && event.getCursor() != null &&
                    event.getClickedInventory() != null &&
                    event.getCursor().getType().equals(ScanBlockHunt.runningGame.getCurrentItem()) &&
                    event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                ScanBlockHunt.runningGame.itemFound(player);
            }
        }
    }
}
