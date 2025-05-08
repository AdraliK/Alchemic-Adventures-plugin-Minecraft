package listeners.world;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

import static adralik.vanillaPlus.Main.config;

public class LinkPlayerManager implements Listener {

    private static final int DARKNESS_EFFECT_TIME = 20 * 60 * 5;
    private static final String UN_AUTH_MESSAGE_NORMAL = config.getString("unauthorized-message.normal", "your text 1");
    private static final String UN_AUTH_MESSAGE_NETHER = config.getString("unauthorized-message.nether", "your text 2");
    private static final String UN_AUTH_MESSAGE_END = config.getString("unauthorized-message.end", "your text 3");
    private static final String PERMISSION = config.getString("authorized-permission", "permission");

    private final HashMap<UUID, BukkitTask> endTasks = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(PERMISSION)) return;

        World.Environment dimension = player.getWorld().getEnvironment();
        applyDimensionEffect(dimension, player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removeTasks(player);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(PERMISSION)) return;

        World.Environment dimension = player.getWorld().getEnvironment();
        removeTasks(player);
        applyDimensionEffect(dimension, player);
    }

    private void applyDimensionEffect(World.Environment dimension, Player player) {
        switch (dimension) {
            case NETHER -> {
                player.sendActionBar(UN_AUTH_MESSAGE_NETHER);
                player.setFireTicks(Integer.MAX_VALUE);
            }
            case THE_END -> {
                player.sendActionBar(UN_AUTH_MESSAGE_END);
                applyTheEndEffect(player);
            }
            default -> {
                player.sendActionBar(UN_AUTH_MESSAGE_NORMAL);
                removeTasks(player);
                removeEffects(player);
            }
        }
    }

    private void applyTheEndEffect(Player player) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.javaPlugin, () -> {
            if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, DARKNESS_EFFECT_TIME, 0));
            }
        }, 0, DARKNESS_EFFECT_TIME);

        endTasks.put(player.getUniqueId(), task);
    }

    private void removeEffects(Player player) {
        player.setFireTicks(0);
        player.removePotionEffect(PotionEffectType.DARKNESS);
    }

    private void removeTasks(Player player) {
        UUID playerId = player.getUniqueId();
        if (endTasks.containsKey(playerId)) {
            endTasks.remove(playerId).cancel();
        }
    }
}
