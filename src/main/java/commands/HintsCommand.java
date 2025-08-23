package commands;

import helpers.HintsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static adralik.vanillaPlus.Main.config;

public class HintsCommand implements CommandExecutor, TabCompleter {

    private static final String configPath = "hints";
    private static final String hintsEnabledMessage = config.getString(configPath + ".hints-enabled-message", "hints enabled");
    private static final String hintsDisabledMessage = config.getString(configPath + ".hints-disabled-message", "hints disabled");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String helpMessage = "Использование: /hints <on|off>";

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эту команду может использовать только игрок!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(helpMessage);
            return true;
        }

        if (args[0].equalsIgnoreCase("on")) {
            HintsManager.enableHints(player.getUniqueId());
            player.sendMessage(hintsEnabledMessage);
        } else if (args[0].equalsIgnoreCase("off")) {
            HintsManager.disableHints(player.getUniqueId());
            player.sendMessage(hintsDisabledMessage);
        } else {
            player.sendMessage(helpMessage);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("on");
            completions.add("off");
        }
        return completions;
    }
}
