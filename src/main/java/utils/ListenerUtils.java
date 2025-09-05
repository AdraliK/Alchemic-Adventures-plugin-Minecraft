package utils;

import org.bukkit.event.Listener;

import static adralik.vanillaPlus.Main.javaPlugin;
import static adralik.vanillaPlus.Main.pluginManager;

public class ListenerUtils {

    public static void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, javaPlugin);
        }
    }
}
