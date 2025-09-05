package utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import static adralik.vanillaPlus.Main.javaPlugin;

public class ListenerUtils {

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, javaPlugin);
        }
    }
}
