package adralik.vanillaPlus;

import commands.CommandManager;
import helpers.HintsManager;

import helpers.Updater;
import listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import placeholders.PlaceHolders;

public final class Main extends JavaPlugin implements Listener {

    public static PluginManager pluginManager;
    public static JavaPlugin javaPlugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        javaPlugin = this;

        getConfig().options().copyDefaults();
        saveDefaultConfig();
        config = javaPlugin.getConfig();

        Listeners.register();
        HintsManager.init();
        CommandManager.init();
        PlaceHolders.init();

        Bukkit.getPluginManager().registerEvents(this, this);

        new Updater().start();

        //DatapackUtils.setupDatapack(this, getServer().getWorldContainer());
    }

    @Override
    public void onDisable() {
        HintsManager.save();
    }
}
