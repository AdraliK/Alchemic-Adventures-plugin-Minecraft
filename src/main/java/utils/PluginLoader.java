package utils;

import commands.CommandManager;
import helpers.HintsManager;
import listeners.Listeners;
import placeholders.PlaceHolders;

import java.util.List;

public class PluginLoader {

    private static final List<Loadable> managers = List.of(
            new Listeners(),
            new HintsManager(),
            new CommandManager(),
            new PlaceHolders()
    );

    public static void register() {
        managers.forEach(Loadable::register);
    }

    public static void shutdown() {
        managers.forEach(Loadable::shutdown);
    }

}
