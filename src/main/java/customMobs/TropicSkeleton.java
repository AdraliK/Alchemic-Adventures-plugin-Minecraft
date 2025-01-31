package customMobs;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
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

    private Biome[] Biomes = new Biome[] {
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
    public void onWarmSkeletonSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() != EntityType.SKELETON) return;

        Skeleton skeleton = (Skeleton) e.getEntity();
        Biome biome = skeleton.getWorld().getBiome(skeleton.getLocation());

        if (!Arrays.asList(Biomes).contains(biome)) return;

        if (random.nextDouble() <= spawnChance) {
            skeleton.getEquipment().setHelmet(new ItemStack(Material.AZALEA));
        }
    }

    @EventHandler
    public void onWarmSkeletonDamagePlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Projectile)) return;
        // Проверяем, что урон действительно нанесен, а не заблокирован щитом
        if (e.getFinalDamage() <= 0) return;

        Player player = (Player) e.getEntity();
        Projectile arrow = (Projectile) e.getDamager();
        ProjectileSource shooter = arrow.getShooter();

        if (!(shooter instanceof Skeleton)) return;

        Skeleton skeleton = (Skeleton) shooter;

        if (skeleton.getEquipment().getHelmet() == null) return;
        if (skeleton.getEquipment().getHelmet().getType() == Material.AZALEA){
            if (random.nextDouble() <= effectChance) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, durationEffect, 1));
            }
        }
    }
}
