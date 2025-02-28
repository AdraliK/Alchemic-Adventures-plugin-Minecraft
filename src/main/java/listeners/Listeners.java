package listeners;

import adralik.vanillaPlus.Main;
import listeners.blocks.AnvilInteract;
import listeners.blocks.CauldronInteract;
import listeners.blocks.ComposterInteract;
import listeners.blocks.StonecutterInteract;
import listeners.items.CustomHeadDrop;
import listeners.items.CustomMinecart;
import listeners.items.SoupFix;
import listeners.items.TotemCooldown;
import listeners.world.*;
import org.bukkit.Bukkit;

import static adralik.vanillaPlus.Main.pluginManager;

public class Listeners {

    public static void init(){
        pluginManager = Bukkit.getPluginManager();

    //------------------BLOCKS-------------------//

        pluginManager.registerEvents(new CauldronInteract(), Main.javaPlugin);
        pluginManager.registerEvents(new ComposterInteract(), Main.javaPlugin);
        pluginManager.registerEvents(new AnvilInteract(), Main.javaPlugin);
        pluginManager.registerEvents(new StonecutterInteract(), Main.javaPlugin);

    //------------------ITEMS--------------------//

        pluginManager.registerEvents(new SoupFix(), Main.javaPlugin);
        pluginManager.registerEvents(new TotemCooldown(), Main.javaPlugin);
        pluginManager.registerEvents(new CustomMinecart(), Main.javaPlugin);
        pluginManager.registerEvents(new CustomHeadDrop(), Main.javaPlugin);

    //------------------WORLD--------------------//

        pluginManager.registerEvents(new MoonPhaseChecker(), Main.javaPlugin);
        pluginManager.registerEvents(new FullMoonMobsBuffs(), Main.javaPlugin);
        pluginManager.registerEvents(new PaleGarden(), Main.javaPlugin);
        pluginManager.registerEvents(new FarmLandProtection(), Main.javaPlugin);
        pluginManager.registerEvents(new Explosions(), Main.javaPlugin);
    }
}
