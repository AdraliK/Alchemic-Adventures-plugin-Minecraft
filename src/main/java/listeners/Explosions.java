package listeners;

import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Explosions implements Listener {
    private final Random random = new Random();
    private final double CHANCE = 0.5;

    Map<EntityType, CraterSettings> craterSettings = Map.of(
            EntityType.CREEPER, new CraterSettings(3, this::getWorldReplacementMaterial),
            EntityType.FIREBALL, new CraterSettings(2, this::getNetherReplacementMaterial),
            EntityType.END_CRYSTAL, new CraterSettings(6, this::getWorldReplacementMaterial)
    );
    private record CraterSettings(int radius, MaterialReplacementRule replacementRule) {}

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        CraterSettings settings = craterSettings.get(event.getEntityType());

        if (settings != null) {
            int radius = calculateExplosionRadius(event);
            modifyCrater(event, radius, settings.replacementRule());
            modifyDrops(event.blockList());
        }
    }

    private int calculateExplosionRadius(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper creeper && creeper.isPowered()) {
            return 6;
        }
        return craterSettings.get(event.getEntityType()).radius;
    }

    private void modifyCrater(EntityExplodeEvent event, int radius, MaterialReplacementRule replacementRule) {
        Location explosionCenter = event.getLocation();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location blockLocation = explosionCenter.clone().add(x, y, z);
                    Block block = blockLocation.getBlock();

                    if (blockLocation.distance(explosionCenter) > radius) continue;
                    if (random.nextDouble() > CHANCE) continue;

                    Material replacement = replacementRule.getReplacement(block.getType());
                    if (replacement != null) {
                        block.setType(replacement);
                    }
                }
            }
        }
    }

    private Material getWorldReplacementMaterial(Material material) {
        return switch (material) {
            case DIRT -> Material.COARSE_DIRT;
            case STONE -> Material.COBBLESTONE;
            case SAND -> Material.SANDSTONE;
            default -> null;
        };
    }

    private Material getNetherReplacementMaterial(Material material) {
        return switch (material) {
          case NETHERRACK -> Material.BLACKSTONE;
            default -> null;
        };
    }

    private void modifyDrops(List<Block> blocks) {
        List<Material> listOfLogs = Arrays.asList(
                Material.OAK_LOG, Material.STRIPPED_OAK_LOG,
                Material.SPRUCE_LOG, Material.STRIPPED_SPRUCE_LOG,
                Material.BIRCH_LOG, Material.STRIPPED_BIRCH_LOG,
                Material.JUNGLE_LOG, Material.STRIPPED_JUNGLE_LOG,
                Material.ACACIA_LOG, Material.STRIPPED_ACACIA_LOG,
                Material.DARK_OAK_LOG, Material.STRIPPED_DARK_OAK_LOG,
                Material.MANGROVE_LOG, Material.STRIPPED_MANGROVE_LOG,
                Material.CHERRY_LOG, Material.STRIPPED_CHERRY_LOG
        );
        for (Block block : blocks) {
            if (listOfLogs.contains(block.getType())){
                block.setType(Material.AIR);
                if (random.nextDouble() < 0.3) {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.CHARCOAL));
                }
            }
        }
    }

    @FunctionalInterface
    private interface MaterialReplacementRule {
        Material getReplacement(Material material);
    }
}

