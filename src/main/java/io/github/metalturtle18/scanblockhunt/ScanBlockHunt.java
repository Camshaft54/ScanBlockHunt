package io.github.metalturtle18.scanblockhunt;

import io.github.metalturtle18.scanblockhunt.commands.CommandManager;
import io.github.metalturtle18.scanblockhunt.events.ItemPickupEvent;
import io.github.metalturtle18.scanblockhunt.events.MoveItemEvent;
import io.github.metalturtle18.scanblockhunt.events.RoundElapsedRunnable;
import io.github.metalturtle18.scanblockhunt.util.Game;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ScanBlockHunt extends JavaPlugin {

    public static Game runningGame; // For now there can be only one game running at a time
    public static boolean roundGoing = false;
    @Getter private static ScanBlockHunt plugin;

    @Override
    public void onEnable() {
        runningGame = null;
        plugin = this;
        PluginManager pluginManager = getServer().getPluginManager();
        Objects.requireNonNull(getCommand("blockhunt")).setExecutor(new CommandManager());
        Objects.requireNonNull(getCommand("blockhunt")).setTabCompleter(new CommandManager());
        getServer().getScheduler().runTaskTimer(this, new RoundElapsedRunnable(), 20L, 20L);
        pluginManager.registerEvents(new ItemPickupEvent(), this);
        pluginManager.registerEvents(new MoveItemEvent(), this);
        Messenger.sendMessage("Plugin loaded successfully!", MessageSeverity.INFO);
    }

    @Override
    public void onDisable() {
        Messenger.sendMessage("Plugin disabled successfully!", MessageSeverity.INFO);
    }
}
