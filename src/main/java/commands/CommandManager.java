package commands;

import utils.Loadable;

import static adralik.vanillaPlus.Main.javaPlugin;

public class CommandManager implements Loadable {

    @Override
    public void register() {
        javaPlugin.getCommand("hints").setExecutor(new HintsCommand());
        javaPlugin.getCommand("hints").setTabCompleter(new HintsCommand());

        javaPlugin.getCommand("givehead").setExecutor(new CustomHeadCommand());
        javaPlugin.getCommand("givehead").setTabCompleter(new CustomHeadCommand());
    }

    @Override
    public void shutdown() {

    }
}
