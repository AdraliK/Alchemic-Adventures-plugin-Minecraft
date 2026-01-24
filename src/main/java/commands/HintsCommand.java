package commands;

import helpers.HintsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static adralik.vanillaPlus.Main.config;

public class HintsCommand implements CommandExecutor {

    private static final String configPath = "hints";
    private static final String hintsEnabledMessage =
            config.getString(configPath + ".hints-enabled-message", "Подсказки включены");
    private static final String hintsDisabledMessage =
            config.getString(configPath + ".hints-disabled-message", "Подсказки выключены");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Эту команду может использовать только игрок!");
            return true;
        }

        if (HintsManager.hasHints(player.getUniqueId())) {
            HintsManager.disableHints(player.getUniqueId());
            player.sendMessage(hintsDisabledMessage);
        } else {
            HintsManager.enableHints(player.getUniqueId());
            player.sendMessage(hintsEnabledMessage);
        }

        return true;
    }
}
