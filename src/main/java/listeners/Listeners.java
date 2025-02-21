package listeners;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;

import static adralik.vanillaPlus.Main.pluginManager;

public class Listeners {

    public static void init(){
        pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new CauldronInteract(), Main.javaPlugin);
        pluginManager.registerEvents(new ComposterInteract(), Main.javaPlugin);
        pluginManager.registerEvents(new AnvilInteract(), Main.javaPlugin);
        pluginManager.registerEvents(new StonecutterInteract(), Main.javaPlugin);

        pluginManager.registerEvents(new SoupFix(), Main.javaPlugin);

        pluginManager.registerEvents(new MoonPhaseChecker(), Main.javaPlugin);
        pluginManager.registerEvents(new FullMoonMobsBuffs(), Main.javaPlugin);
        pluginManager.registerEvents(new PaleGarden(), Main.javaPlugin);

        pluginManager.registerEvents(new TotemCooldown(), Main.javaPlugin);
        pluginManager.registerEvents(new FarmLandProtection(), Main.javaPlugin);

        pluginManager.registerEvents(new Explosions(), Main.javaPlugin);
        pluginManager.registerEvents(new CustomMinecart(), Main.javaPlugin);
    }
}
