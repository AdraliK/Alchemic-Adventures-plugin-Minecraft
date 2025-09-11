package utils;

import commands.CommandManager;
import helpers.HintsManager;
import helpers.HintsUpdater;
import listeners.Listeners;
import placeholders.PlaceHolders;
import recipes.CustomRecipes;

import java.util.List;

public class PluginLoader {

    private static final List<Loadable> managers = List.of(
            new Listeners(),
            new HintsManager(),
            new CommandManager(),
            new PlaceHolders(),
            new HintsUpdater(),
            new CustomRecipes()
    );

    public static void register() {
        managers.forEach(Loadable::register);
    }

    public static void shutdown() {
        managers.forEach(Loadable::shutdown);
    }

}
