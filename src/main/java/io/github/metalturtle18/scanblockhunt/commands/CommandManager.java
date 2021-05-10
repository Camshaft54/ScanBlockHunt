package io.github.metalturtle18.scanblockhunt.commands;

import io.github.metalturtle18.scanblockhunt.util.BlockHuntCommand;
import io.github.metalturtle18.scanblockhunt.util.Messenger;
import io.github.metalturtle18.scanblockhunt.util.enums.CommandTabCompletion;
import io.github.metalturtle18.scanblockhunt.util.enums.MessageSeverity;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final ArrayList<BlockHuntCommand> commands = new ArrayList<>();

    public CommandManager() {
        commands.add(new NewGameCommand());
        commands.add(new GameInfoCommand());
        commands.add(new NextItemCommand());
        commands.add(new EndGameCommand());
        commands.add(new EndRoundCommand());
        commands.add(new AddPlayerCommand());
        commands.add(new RemovePlayerCommand());
        commands.add(new ResetCommand());
        commands.add(new SetScoreCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command c, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            Messenger.sendMessage("This command can be run only by players!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return true;
        } else if (args.length <= 0) {
            Messenger.sendMessage("You need to specify a subcommand!", MessageSeverity.INCORRECT_COMMAND_USAGE);
            return true;
        }
        Player player = (Player) commandSender;
        for (BlockHuntCommand command : commands) {
            if (command.getCommandName().equalsIgnoreCase(args[0])) {
                command.runCommand(player, args);
                return true;
            }
        }
        Messenger.sendMessage(player, "Invalid subcommand!", MessageSeverity.INCORRECT_COMMAND_USAGE);
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String s, @NonNull String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (CommandTabCompletion cmd : CommandTabCompletion.values()) {
                completions.add(cmd.name().toLowerCase());
            }
        } else if (CommandTabCompletion.get(args[0]) != null) {
            String[][] cmdArgs = CommandTabCompletion.get(args[0]).getArgs();
            if (cmdArgs.length > args.length - 2) {
                if (cmdArgs[args.length-2].length > 0 && cmdArgs[args.length-2][0].equals("*player*")) {
                    completions.addAll(getPlayerList(args[args.length-2]));
                } else if (cmdArgs[args.length-2].length > 0 && cmdArgs[args.length-2][0].equals("*material*")) {
                    completions.addAll(getMaterialList(args[args.length-2]));
                } else {
                    completions.addAll(Arrays.asList(cmdArgs[args.length - 2]));
                }
            }
        }
        final List<String> finalCompletions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length-1], completions, finalCompletions);
        Collections.sort(finalCompletions);
        return finalCompletions;
    }

    public ArrayList<String> getMaterialList(String argument) {
        System.out.println("test");
        ArrayList<String> out = new ArrayList<>();
        List<Material> materialList = Arrays.asList(Material.values());
        ArrayList<String> materials = new ArrayList<>();
        materialList.forEach(material -> {if (material.isItem()) materials.add(material.toString().toLowerCase());});
        StringUtil.copyPartialMatches(argument, materials, out);
        Collections.sort(out);
        return out;
    }

    public ArrayList<String> getPlayerList(String argument) {
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getDisplayName()));
        StringUtil.copyPartialMatches(argument, players, out);
        Collections.sort(out);
        return out;
    }
}
