package commands;

import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomHeadCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage("Использование: /givehead <player> <head>");
            return true;
        }

        String playerName = args[0];
        String headName = args[1].toUpperCase();

        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            player.sendMessage("Игрок не найден: " + playerName);
            return true;
        }

        HeadType headType;
        try {
            headType = HeadType.valueOf(headName);
        } catch (IllegalArgumentException e) {
            player.sendMessage("Неизвестная голова: " + args[1]);
            return true;
        }

        if (target.isOnline()) {
            ItemStack head = CustomHead.createHead(headType);
            target.getPlayer().getInventory().addItem(head);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.stream(HeadType.values())
                    .map(HeadType::name)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
