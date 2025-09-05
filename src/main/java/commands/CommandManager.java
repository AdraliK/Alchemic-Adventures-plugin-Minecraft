package commands;

import utils.Loadable;

import static adralik.vanillaPlus.Main.javaPlugin;

public class CommandManager implements Loadable {

    @Override
    public void register() {
        javaPlugin.getCommand("hints").setExecutor(new HintsCommand());
        javaPlugin.getCommand("hints").setTabCompleter(new HintsCommand());
    }

    @Override
    public void shutdown() {

    }
}
