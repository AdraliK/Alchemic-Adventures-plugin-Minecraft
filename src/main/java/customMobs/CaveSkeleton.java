package customMobs;

import org.bukkit.Material;
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

import java.util.Random;

import static adralik.alchemicAndAdventures.Main.config;

public class CaveSkeleton implements Listener {

    private final String configPath = "custom-mobs.cave-skeleton";

    private final double spawnChance = config.getDouble(configPath + ".spawn-chance");
    private final double effectChance = config.getDouble(configPath + ".effect-chance");
    private final int durationEffect = config.getInt(configPath + ".duration-effect");

    private final Random random = new Random();

    @EventHandler
    public void onCaveSkeletonSpawn(CreatureSpawnEvent e) {
        if (e.getEntityType() != EntityType.SKELETON) return;

        Skeleton skeleton = (Skeleton) e.getEntity();
        if (skeleton.getLocation().getY() < 55) {
            if (random.nextDouble() <= spawnChance) {
                skeleton.getEquipment().setHelmet(new ItemStack(Material.MEDIUM_AMETHYST_BUD));
            }
        }
    }

    @EventHandler
    public void onCaveSkeletonDamagePlayer(EntityDamageByEntityEvent e) {
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
        if (skeleton.getEquipment().getHelmet().getType() == Material.MEDIUM_AMETHYST_BUD){
            if (random.nextDouble() <= effectChance) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, durationEffect, 0));
            }
        }
    }
}
