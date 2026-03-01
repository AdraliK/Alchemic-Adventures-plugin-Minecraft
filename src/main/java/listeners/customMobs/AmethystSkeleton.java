package listeners.customMobs;

import helpers.DatapackUtils;
import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Random;
import java.util.Set;

import static adralik.vanillaPlus.Main.config;

public class AmethystSkeleton implements Listener {

    private final Random random = new Random();
    private final String configPath = "custom-mobs.amethyst-skeleton";
    private static final double UNDERGROUND_DEPTH_HELMET_DROP_CHANCE = 0.03;

    @EventHandler
    public void onAmethystSkeletonSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() != EntityType.SKELETON) return;

        Skeleton skeleton = (Skeleton) event.getEntity();
        if (skeleton.getWorld().getEnvironment() != World.Environment.NORMAL) return;

        double spawnY = skeleton.getLocation().getY();
        double spawnChance = config.getDouble(configPath + ".spawn-chance");

        if (spawnY <= 55 && random.nextDouble() <= spawnChance){
            int level = getSkeletonLevel(spawnY);
            setSkeletonHealth(skeleton, level);
            assignSkeletonHelmet(skeleton, level);
            skeleton.addScoreboardTag("amethyst_skeleton_" + level);
        }
    }

    @EventHandler
    public void onAmethystSkeletonDamagePlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getDamager() instanceof Projectile arrow)) return;
        if (player.isBlocking()) return;

        ProjectileSource shooter = arrow.getShooter();

        if (!(shooter instanceof Skeleton skeleton)) return;

        int skeletonLevel = getSkeletonLevelByHelmet(skeleton);
        if (skeletonLevel == -1) return;

        double effectChance = config.getDouble(configPath + ".levels." + skeletonLevel + ".effect-chance");
        int durationEffect = config.getInt(configPath + ".levels." + skeletonLevel + ".base-duration");
        int maxDurationEffect = config.getInt(configPath + ".levels." + skeletonLevel + ".max-duration");

        ItemStack helmet = player.getInventory().getHelmet();
        if (random.nextDouble() <= effectChance && !CustomHead.typeIs(helmet, HeadType.MINER_HELMET)) {
            applyPotionEffect(player, durationEffect, maxDurationEffect);
            DatapackUtils.grantAdvancement(player, "amethyst_skeleton_effect");
        }
    }

    @EventHandler
    public void onAmethystSkeletonDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Skeleton skeleton)) return;
        if (!(isAmethystSkeleton(skeleton))) return;

        int level = getSkeletonLevelByHelmet(skeleton);
        double dropChance = config.getDouble(configPath + ".levels." + level + ".drop-chance");

        if (random.nextDouble() <= dropChance) {
            event.getDrops().add(new ItemStack(Material.AMETHYST_SHARD));
        }
        if (level == 4 && random.nextDouble() <= UNDERGROUND_DEPTH_HELMET_DROP_CHANCE) {
            event.getDrops().add(CustomHead.createHead(HeadType.UNDERGROUND_DEPTH_HELMET));
        }
    }

    public void applyPotionEffect(Player player, int durationEffect, int maxDurationEffect) {
        PotionEffectType effectType = PotionEffectType.BLINDNESS;
        int newDuration = durationEffect;

        if (player.hasPotionEffect(effectType)) {
            PotionEffect currentEffect = player.getPotionEffect(effectType);
            if (currentEffect != null) {
                newDuration += currentEffect.getDuration();
            }
        }
        if (newDuration > maxDurationEffect) {
            newDuration = maxDurationEffect;
        }

        player.addPotionEffect(new PotionEffect(effectType, newDuration, 0));
    }

    private int getSkeletonLevel(double depthY){
        int depth = getDepthLevel(depthY);
        MemorySection spawnChancesSection = (MemorySection) config.get(configPath + ".spawn-chances-by-depth.depth-" + depth);

        if (spawnChancesSection == null) {
            return -1;
        }

        Set<String> spawnChances = spawnChancesSection.getKeys(false);

        while (true) {
            for (String level : spawnChances) {
                Object value = spawnChancesSection.get(level);
                int skeletonLevel = Integer.parseInt(level);
                double chance = (Double) value;

                if (random.nextDouble() <= chance) {
                    return skeletonLevel;
                }
            }
        }
    }

    private int getDepthLevel(double y){
        if (y >= 28 && y <= 55) return 1;
        if (y >= 0 && y < 28) return 2;
        if (y >= -27 && y < 0) return 3;
        if (y >= -55 && y < -27) return 4;
        return -1;
    }

    private void setSkeletonHealth(Skeleton skeleton, int level){
        int health = config.getInt(configPath + ".levels." + level + ".health");
        skeleton.getAttribute(Attribute.MAX_HEALTH).setBaseValue(health);
        skeleton.setHealth(health);
    }

    private void assignSkeletonHelmet(Skeleton skeleton, int level) {
        Material helmetMaterial = switch (level) {
            case 1 -> Material.SMALL_AMETHYST_BUD;
            case 2 -> Material.MEDIUM_AMETHYST_BUD;
            case 3 -> Material.LARGE_AMETHYST_BUD;
            case 4 -> Material.AMETHYST_CLUSTER;
            default -> null;
        };
        if (helmetMaterial != null) {
            skeleton.getEquipment().setHelmet(new ItemStack(helmetMaterial));
        }
    }

    private int getSkeletonLevelByHelmet(Skeleton skeleton) {
        if (skeleton.getEquipment().getHelmet() == null) return -1;
        return switch (skeleton.getEquipment().getHelmet().getType()) {
            case SMALL_AMETHYST_BUD -> 1;
            case MEDIUM_AMETHYST_BUD -> 2;
            case LARGE_AMETHYST_BUD -> 3;
            case AMETHYST_CLUSTER -> 4;
            default -> -1;
        };
    }

    private boolean isAmethystSkeleton(Skeleton skeleton) {
        if (skeleton.getEquipment().getHelmet() == null) return false;
        return switch (skeleton.getEquipment().getHelmet().getType()) {
            case SMALL_AMETHYST_BUD,
                 MEDIUM_AMETHYST_BUD,
                 LARGE_AMETHYST_BUD,
                 AMETHYST_CLUSTER -> true;
            default -> false;
        };
    }
}

