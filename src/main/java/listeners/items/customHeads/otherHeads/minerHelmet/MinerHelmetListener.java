package listeners.items.customHeads.otherHeads.minerHelmet;

import adralik.vanillaPlus.Main;
import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MinerHelmetListener implements Listener {

    private final Random random = new Random();
    private final Set<UUID> targetPlayers = new HashSet<>();
    private static final double GENERATE_CHANCE = 0.2;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        updateEffect(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        targetPlayers.remove(e.getEntity().getUniqueId());
    }

    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent e) {
        updateEffect(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDrinkMilk(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() == Material.MILK_BUCKET) {
            Bukkit.getScheduler().runTask(Main.javaPlugin, () -> {
                updateEffect(player);
            });
        }
    }

    private void updateEffect(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (CustomHead.typeIs(helmet, HeadType.MINER_HELMET)) {
            addNightVisionEffect(player);
        } else if (targetPlayers.contains(player.getUniqueId())) {
            removeNightVisionEffect(player);
        }
    }

    private void addNightVisionEffect(Player player) {
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.NIGHT_VISION,
                PotionEffect.INFINITE_DURATION,
                0,
                true, false, true
        ));
        targetPlayers.add(player.getUniqueId());
    }

    private void removeNightVisionEffect(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        targetPlayers.remove(player.getUniqueId());
    }

    @EventHandler
    public void onLootGenerate(LootGenerateEvent e) {
        if (random.nextDouble() > GENERATE_CHANCE) return;
        if (!(e.getLootContext().getLootedEntity() instanceof Player player)) return;

        Entity target = player.getTargetEntity(5);

        if (target != null && target.getType() == EntityType.CHEST_MINECART) {
            e.getLoot().add(CustomHead.createHead(HeadType.MINER_HELMET));
        }
    }
}
