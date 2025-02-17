package listeners;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PaleGarden implements Listener {

    public PaleGarden(){
        updateFog();
    }

    private void updateFog() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Biome biome = player.getLocation().getBlock().getBiome();
                    if (biome == Biome.PALE_GARDEN) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 100, 0, false, false, false));
                    }
                }
            }
        }.runTaskTimer(Main.javaPlugin, 0L, 60L);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity victim = event.getEntity();

        if (damager.getType() == EntityType.CREAKING) {
            if (victim instanceof Player) {
                event.setDamage(12.0);
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity.getLocation().getBlock().getBiome() == Biome.PALE_GARDEN) {
            if (!(event.getEntityType() == EntityType.CREAKING || event.getEntityType() == EntityType.ARMOR_STAND)) {
                event.setCancelled(true);
            }
        }
    }
}
