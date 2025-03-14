package customMobs;

import helpers.DatapackUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.Random;

import static adralik.vanillaPlus.Main.config;

public class TropicSkeleton implements Listener {

    private final String configPath = "custom-mobs.tropic-skeleton";

    private final double spawnChance = config.getDouble(configPath + ".spawn-chance");
    private final double effectChance = config.getDouble(configPath + ".effect-chance");
    private final int durationEffect = config.getInt(configPath + ".duration-effect");

    private final Random random = new Random();

    private Biome[] biomes = new Biome[] {
            Biome.DESERT,
            Biome.SAVANNA,
            Biome.SAVANNA_PLATEAU,
            Biome.WINDSWEPT_SAVANNA,
            Biome.BADLANDS,
            Biome.WOODED_BADLANDS,
            Biome.ERODED_BADLANDS,
            Biome.JUNGLE,
            Biome.BAMBOO_JUNGLE,
            Biome.SPARSE_JUNGLE
    };

    @EventHandler
    public void onTropicSkeletonSpawn(CreatureSpawnEvent e) {
        if (random.nextDouble() > spawnChance) return;
        if (e.getEntityType() != EntityType.SKELETON) return;

        Skeleton skeleton = (Skeleton) e.getEntity();
        Biome biome = skeleton.getWorld().getBiome(skeleton.getLocation());

        if (!Arrays.asList(biomes).contains(biome)) return;
        skeleton.getEquipment().setHelmet(new ItemStack(Material.AZALEA));
        skeleton.addScoreboardTag("tropic_skeleton");
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (random.nextDouble() > effectChance) return;

        ProjectileSource shooter = event.getEntity().getShooter();

        if (!(shooter instanceof Skeleton skeleton)) return;
        if (isNotTropicSkeleton(skeleton)) return;
        if (event.getHitEntity() instanceof Player player && player.isBlocking()) return;

        Location hitLocation = event.getEntity().getLocation();

        if (event.getHitEntity() instanceof LivingEntity) {
            hitLocation = event.getHitEntity().getLocation();
        }
        if (event.getHitEntity() instanceof Player player) {
            DatapackUtils.grantAdvancement(player, "tropic_skeleton_effect");
        }

        ItemStack potionItem = getLingeringPoisonPotion();
        splashPotion(hitLocation, potionItem);
    }

    private void splashPotion(Location hitLocation, ItemStack potionItem) {
        ThrownPotion thrownPotion = hitLocation.getWorld().spawn(hitLocation, ThrownPotion.class);
        thrownPotion.setItem(potionItem);
        thrownPotion.splash();
    }

    private ItemStack getLingeringPoisonPotion() {
        ItemStack potionItem = new ItemStack(Material.LINGERING_POTION);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();

        potionMeta.addCustomEffect(new PotionEffect(
                PotionEffectType.POISON,
                durationEffect,
                1
        ), true);

        potionItem.setItemMeta(potionMeta);
        return potionItem;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;

        if (!(arrow.getShooter() instanceof Skeleton skeleton)) return;
        if (isNotTropicSkeleton(skeleton)) return;

        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.isBlocking()) return;

        arrow.remove();
    }

    private boolean isNotTropicSkeleton(Skeleton skeleton) {
        if (skeleton.getEquipment().getHelmet() == null) return true;
        return skeleton.getEquipment().getHelmet().getType() != Material.AZALEA;
    }
}
