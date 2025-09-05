package listeners.blocks;

import helpers.CampfireManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static adralik.vanillaPlus.Main.javaPlugin;

public class CampfireInteract implements Listener {

    private static final double RADIUS = 16;
    private static final long UPDATE_PERIOD = 3 * 20L;
    private static final int REGENERATION_EFFECT_TIME = (int) UPDATE_PERIOD + 20;

    private static final PotionEffect CAMPFIRE_REGEN = new PotionEffect(
            PotionEffectType.REGENERATION,
            REGENERATION_EFFECT_TIME,
            0,
            true,
            false,
            true
    );

    public CampfireInteract() {
        startCampfireEffectTask();
    }

    private void startCampfireEffectTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                checkPlayersNearCampfire();
            }
        }.runTaskTimer(javaPlugin, 0L, UPDATE_PERIOD);
    }

    private void checkPlayersNearCampfire() {
        for (Location campfireLoc : CampfireManager.getCampfires()) {
            if (removeIfCampfireWentOut(campfireLoc)) {
                continue;
            }
            for (Entity entity : campfireLoc.getNearbyEntities(RADIUS, RADIUS, RADIUS)) {
                if (entity instanceof Player player) {
                    if (isPlayerInRadius(campfireLoc, player)) {
                        player.addPotionEffect(CAMPFIRE_REGEN);
                    }
                }
            }
        }
    }

    private boolean removeIfCampfireWentOut(Location campfireLoc) {
        Campfire campfire = (Campfire) campfireLoc.getBlock().getBlockData();
        if (!campfire.isLit()) {
            CampfireManager.removeCampfire(campfireLoc);
            return true;
        }
        return false;
    }

    private boolean isPlayerInRadius(Location campfireLoc, Player player) {
        return player.getLocation().distanceSquared(campfireLoc) <= RADIUS * RADIUS;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        handleCampfireAdded(event.getBlockPlaced());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        handleCampfireRemoved(event.getBlock());
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            handleCampfireRemoved(block);
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();
        if (isCampfire(block)) {
            Campfire campfire = (Campfire) block.getBlockData();
            if (campfire.isLit()) {
                handleCampfireAdded(block);
            }
        }
    }

    private void handleCampfireAdded(Block block) {
        if (isCampfire(block)) {
            CampfireManager.addCampfire(block.getLocation());
        }
    }

    private void handleCampfireRemoved(Block block) {
        if (isCampfire(block)) {
            CampfireManager.removeCampfire(block.getLocation());
        }
    }

    private boolean isCampfire(Block block) {
        return block.getBlockData() instanceof Campfire;
    }
}
