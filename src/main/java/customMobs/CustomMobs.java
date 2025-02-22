package customMobs;

import adralik.vanillaPlus.Main;

import static adralik.vanillaPlus.Main.pluginManager;

public class CustomMobs {

    public static void init(){
        pluginManager.registerEvents(new MobsNewMechanics(), Main.javaPlugin);
        pluginManager.registerEvents(new SpiderWebShooter(), Main.javaPlugin);
        pluginManager.registerEvents(new AmethystSkeleton(), Main.javaPlugin);
        pluginManager.registerEvents(new TropicSkeleton(), Main.javaPlugin);
    }

}
