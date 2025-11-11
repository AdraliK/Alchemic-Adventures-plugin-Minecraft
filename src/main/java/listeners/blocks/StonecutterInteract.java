package listeners.blocks;

import adralik.vanillaPlus.Main;
import helpers.DatapackUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StonecutterInteract implements Listener {

    private final Set<UUID> playersOnStonecutter = new HashSet<>();

    public StonecutterInteract() {
        startUpdate();
    }

    private void startUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkMobsNearStonecutters();
            }
        }.runTaskTimer(Main.javaPlugin, 0L, 10L);
    }

    private void checkMobsNearStonecutters() {
        for (World world : Bukkit.getWorlds()) {
            if (world.getPlayers().isEmpty()) continue;

            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    if (!(entity instanceof LivingEntity livingEntity) || entity instanceof Player) {
                        continue;
                    }
                    boolean isEntityOnStonecutter =
                            livingEntity.getLocation().getBlock().getType() == Material.STONECUTTER;

                    if (isEntityOnStonecutter) {
                        livingEntity.damage(1.0);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        damagePlayerFromStonecutter(player);
    }

    private void damagePlayerFromStonecutter(Player player) {
        UUID entityUUID = player.getUniqueId();
        boolean isOnStonecutter = player.getLocation().getBlock().getType() == Material.STONECUTTER;

        if (isOnStonecutter) {
            playersOnStonecutter.add(entityUUID);
            player.damage(1.0); // Моментальный урон

            DatapackUtils.grantAdvancement(player, "stonecutter_damage");

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!playersOnStonecutter.contains(entityUUID)) {
                        cancel();
                        return;
                    }
                    player.damage(1.0);
                }
            }.runTaskTimer(Main.javaPlugin, 0L, 10L);

        } else {
            playersOnStonecutter.remove(entityUUID);
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
