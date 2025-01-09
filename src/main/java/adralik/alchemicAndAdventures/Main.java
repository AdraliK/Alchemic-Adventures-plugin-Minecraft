package adralik.alchemicAndAdventures;

import customMobs.CustomMobs;
import listeners.Listeners;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

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
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
