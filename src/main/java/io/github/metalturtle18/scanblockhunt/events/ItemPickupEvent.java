package io.github.metalturtle18.scanblockhunt.events;

import io.github.metalturtle18.scanblockhunt.ScanBlockHunt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickupEvent implements Listener {

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player) || ScanBlockHunt.runningGame.getCurrentItem() == null) return;
        Player player = (Player) event.getEntity();
        if (event.getItem().getItemStack().getType().equals(ScanBlockHunt.runningGame.getCurrentItem()) && ScanBlockHunt.roundGoing) {
            ScanBlockHunt.runningGame.itemFound(player);
            player.sendMessage("found the item"); // TODO: testing
        }
    }
}
