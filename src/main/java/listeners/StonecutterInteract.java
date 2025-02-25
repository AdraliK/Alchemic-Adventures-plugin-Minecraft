package listeners;

import adralik.vanillaPlus.Main;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StonecutterInteract implements Listener {

    private final Set<UUID> entitiesOnStonecutter = new HashSet<>();

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

    private void damageEntityFromStonecutter(LivingEntity livingEntity) {
        UUID entityUUID = livingEntity.getUniqueId();
        boolean isOnStonecutter = livingEntity.getLocation().getBlock().getType() == Material.STONECUTTER;

        if (isOnStonecutter) {
            entitiesOnStonecutter.add(entityUUID);
            livingEntity.damage(1.0); // Моментальный урон

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!entitiesOnStonecutter.contains(entityUUID)) {
                        cancel();
                        return;
                    }
                    livingEntity.damage(1.0);
                }
            }.runTaskTimer(Main.javaPlugin, 0L, 10L); // Урон каждую секунду

        } else {
            entitiesOnStonecutter.remove(entityUUID);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        UUID livingEntityUUID = livingEntity.getUniqueId();

        if (livingEntity.getLocation().getBlock().getType() == Material.STONECUTTER) {
            entitiesOnStonecutter.remove(livingEntityUUID);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getLocation().getBlock().getType() == Material.STONECUTTER) {
            event.setDeathMessage(player.getName() + " был безжалостно разрезан камнерезом!");
        }
    }
}
