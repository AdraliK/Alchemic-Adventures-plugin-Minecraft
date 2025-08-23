package commands;

import static adralik.vanillaPlus.Main.javaPlugin;

public class CommandManager {

    public static void init() {
        javaPlugin.getCommand("hints").setExecutor(new HintsCommand());
        javaPlugin.getCommand("hints").setTabCompleter(new HintsCommand());
    }
}
