package customMobs;

import adralik.alchemicAndAdventures.Main;

import static adralik.alchemicAndAdventures.Main.pluginManager;

public class CustomMobs {

    public static void init(){
        pluginManager.registerEvents(new MobsNewMechanics(), Main.javaPlugin);
        pluginManager.registerEvents(new SpiderWebShooter(), Main.javaPlugin);
        pluginManager.registerEvents(new CaveSkeleton(), Main.javaPlugin);
        pluginManager.registerEvents(new TropicSkeleton(), Main.javaPlugin);
    }

}
