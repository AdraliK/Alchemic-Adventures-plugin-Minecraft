package adralik.vanillaPlus;

import helpers.Updater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import utils.PluginLoader;

public final class Main extends JavaPlugin implements Listener {

    public static JavaPlugin javaPlugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        javaPlugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        config = javaPlugin.getConfig();

        PluginLoader.register();

        Bukkit.getPluginManager().registerEvents(this, this);

        new Updater().start();

        //DatapackUtils.setupDatapack(this, getServer().getWorldContainer());
    }

    @Override
    public void onDisable() {
        PluginLoader.shutdown();
    }
}
