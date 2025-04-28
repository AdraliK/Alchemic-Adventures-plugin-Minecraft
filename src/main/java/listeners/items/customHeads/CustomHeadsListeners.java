package listeners.items.customHeads;

import adralik.vanillaPlus.Main;
import listeners.items.customHeads.fullMoonHeads.LeakyBag;
import listeners.items.customHeads.fullMoonHeads.RadiantHelmet;
import listeners.items.customHeads.fullMoonHeads.PieceCheese;

import static adralik.vanillaPlus.Main.pluginManager;

public class CustomHeadsListeners {

    public static void init(){
        pluginManager.registerEvents(new PieceCheese(), Main.javaPlugin);
        pluginManager.registerEvents(new RadiantHelmet(), Main.javaPlugin);
        pluginManager.registerEvents(new LeakyBag(), Main.javaPlugin);
    }

}
