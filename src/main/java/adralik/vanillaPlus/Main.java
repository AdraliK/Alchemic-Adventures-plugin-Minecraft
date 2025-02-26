package adralik.vanillaPlus;

import customMobs.CustomMobs;
import listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin implements Listener {

    public static PluginManager pluginManager;
    public static JavaPlugin javaPlugin;
    public static FileConfiguration config;
    private static Main instance;

    @Override
    public void onEnable() {
        javaPlugin = this;
        instance = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        config = javaPlugin.getConfig();

        Listeners.init();
        CustomMobs.init();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setInvulnerable(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setInvulnerable(false);
            }
        }.runTaskLater(this, 20 * 5);
    }
}
