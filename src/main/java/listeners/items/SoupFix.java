package listeners.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class SoupFix implements Listener {

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.RABBIT_STEW) {
            Player player = e.getPlayer();
            player.setSaturation(14.0f);
        }
        else if (e.getItem().getType() == Material.BEETROOT_SOUP || e.getItem().getType() == Material.MUSHROOM_STEW) {
            Player player = e.getPlayer();
            player.setFoodLevel(Math.min(player.getFoodLevel() + 7, 20));
            player.setSaturation(12.0f);
            e.setCancelled(true);
            player.getInventory().setItemInHand(new ItemStack(Material.BOWL));
        }
    }
}
