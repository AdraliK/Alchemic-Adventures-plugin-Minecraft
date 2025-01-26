package listeners;

import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Map;
import java.util.Random;

public class Explosions implements Listener {
    private final Random random = new Random();
    private final double CHANCE = 0.5;

    Map<EntityType, CraterSettings> craterSettings = Map.of(
            EntityType.CREEPER, new CraterSettings(4, this::getWorldReplacementMaterial),
            EntityType.FIREBALL, new CraterSettings(2, this::getNetherReplacementMaterial),
            EntityType.END_CRYSTAL, new CraterSettings(6, this::getWorldReplacementMaterial)
    );
    private record CraterSettings(int radius, MaterialReplacementRule replacementRule) {}

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        CraterSettings settings = craterSettings.get(event.getEntityType());
        if (settings != null) {
            modifyCrater(event, settings.radius(), settings.replacementRule());
        }
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
            case OAK_LOG, SPRUCE_LOG, BIRCH_LOG, JUNGLE_LOG, ACACIA_LOG, DARK_OAK_LOG, MANGROVE_LOG, CHERRY_LOG ->
                    Material.COAL_BLOCK;
            default -> null;
        };
    }

    private Material getNetherReplacementMaterial(Material material) {
        return switch (material) {
          case NETHERRACK -> Material.BLACKSTONE;
            default -> null;
        };
    }

    @FunctionalInterface
    private interface MaterialReplacementRule {
        Material getReplacement(Material material);
    }
}

