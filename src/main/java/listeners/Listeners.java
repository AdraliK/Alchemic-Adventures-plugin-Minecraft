package listeners;

import adralik.alchemicAndAdventures.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static adralik.alchemicAndAdventures.Main.pluginManager;

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

        pluginManager.registerEvents(new TotemCooldown(), Main.javaPlugin);
        pluginManager.registerEvents(new FarmLandProtection(), Main.javaPlugin);

    }
}
