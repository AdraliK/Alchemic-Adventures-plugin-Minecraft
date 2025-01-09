package listeners;

import adralik.alchemicAndAdventures.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class StonecutterInteract implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        boolean isOnStonecutter = player.getLocation().getBlock().getType() == Material.STONECUTTER;

        if (isOnStonecutter && !player.hasMetadata("onStonecutter")) {
            player.setMetadata("onStonecutter", new FixedMetadataValue(Main.javaPlugin, true));
            player.damage(1.0); // Моментальный урон

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.hasMetadata("onStonecutter")) {
                        cancel();
                        return;
                    }
                    player.damage(1.0);
                }
            }.runTaskTimer(Main.javaPlugin, 20L, 20L); // Урон каждую секунду
        } else if (!isOnStonecutter && player.hasMetadata("onStonecutter")) {
            player.removeMetadata("onStonecutter", Main.javaPlugin);
        }
    }
}
