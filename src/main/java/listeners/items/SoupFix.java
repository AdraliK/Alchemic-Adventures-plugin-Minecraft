package listeners.items;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class SoupFix implements Listener {

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() == Material.RABBIT_STEW) {
            player = e.getPlayer();
            player.setSaturation(14.0f);
        }
        else if (e.getItem().getType() == Material.BEETROOT_SOUP || e.getItem().getType() == Material.MUSHROOM_STEW) {
            int beforeFood = player.getFoodLevel();
            Player finalPlayer = player;
            Bukkit.getScheduler().runTask(Main.javaPlugin, () -> {
                finalPlayer.setFoodLevel(Math.min(beforeFood + 10, 20));
                finalPlayer.setSaturation(12.0f);
            });
        }
    }
}
