package commands;

import helpers.HintsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static adralik.vanillaPlus.Main.config;

public class HintsCommand implements CommandExecutor {

    private static final String configPath = "hints";
    private static final List<String> hintsEnabledMessage =
            config.getStringList(configPath + ".hints-enabled-message");
    private static final List<String> hintsDisabledMessage =
            config.getStringList(configPath + ".hints-disabled-message");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эту команду может использовать только игрок!");
            return true;
        }

        if (HintsManager.hasHints(player.getUniqueId())) {
            HintsManager.disableHints(player.getUniqueId());
            hintsDisabledMessage.forEach(player::sendMessage);
        } else {
            HintsManager.enableHints(player.getUniqueId());
            hintsEnabledMessage.forEach(player::sendMessage);
        }

        return true;
    }
}
