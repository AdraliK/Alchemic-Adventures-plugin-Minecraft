package listeners.world;

import utils.ListenerRegister;
import utils.ListenerUtils;

public class WorldListeners implements ListenerRegister {
    @Override
    public void register() {
        ListenerUtils.registerListeners(
                new MoonPhaseChecker(),
                new FullMoonMobsBuffs(),
                new PaleGarden(),
                new FarmLandProtection(),
                new Explosions()
        );
    }
}
