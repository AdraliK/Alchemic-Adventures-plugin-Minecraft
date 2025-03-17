package customMobs;

import helpers.DatapackUtils;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

import static adralik.vanillaPlus.Main.config;

public class MobsNewMechanics implements Listener {

    private final int batDurationEffect = config.getInt("custom-mobs.bat.duration-effect");
    private final int zombieDurationEffect = config.getInt("custom-mobs.zombie.duration-effect");
    private final double zombieEffectChance = config.getDouble("custom-mobs.zombie.effect-chance");
    private final Random random = new Random();
    private final int[] expRandom = {3, 3, 4};

    @EventHandler
    public void onBatDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Bat)) return;

        Player player = e.getEntity().getKiller();
        if (player == null) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, batDurationEffect, 0));
    }

    @EventHandler
    public void onZombieDamagePlayer(EntityDamageByEntityEvent e) {
        if (random.nextDouble() > zombieEffectChance) return;
        if (!(e.getEntity() instanceof Player player)) return;
        if (e.getDamager().getType() != EntityType.ZOMBIE) return;
        if (e.getFinalDamage() <= 0) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, zombieDurationEffect, 5));
        DatapackUtils.grantAdvancement(player, "zombie_damage");
    }

    @EventHandler
    public void onSilverfishDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Silverfish) {
            int exp = random.nextInt(expRandom.length);
            event.setDroppedExp(exp);
        }
    }
}
