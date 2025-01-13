package listeners;

import adralik.alchemicAndAdventures.Main;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class StonecutterInteract implements Listener {

    @EventHandler
    public void onEntityMove(EntityMoveEvent e) {
        Entity entity = e.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) {
            return;
        }
        damageEntityFromStonecutter(livingEntity);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        damageEntityFromStonecutter(player);
    }

    private static void damageEntityFromStonecutter(LivingEntity livingEntity) {
        boolean isOnStonecutter = livingEntity.getLocation().getBlock().getType() == Material.STONECUTTER;

        if (isOnStonecutter && !livingEntity.hasMetadata("onStonecutter")) {
            livingEntity.setMetadata("onStonecutter", new FixedMetadataValue(Main.javaPlugin, true));
            livingEntity.damage(1.0); // Моментальный урон

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!livingEntity.hasMetadata("onStonecutter")) {
                        cancel();
                        return;
                    }
                    livingEntity.damage(1.0);
                }
            }.runTaskTimer(Main.javaPlugin, 20L, 20L); // Урон каждую секунду
        } else if (!isOnStonecutter && livingEntity.hasMetadata("onStonecutter")) {
            livingEntity.removeMetadata("onStonecutter", Main.javaPlugin);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.getLocation().getBlock().getType() == Material.STONECUTTER) {
            player.removeMetadata("onStonecutter", Main.javaPlugin);
            event.setDeathMessage(player.getName() + " был безжалостно разрезан камнерезом!");
        }
    }
}
