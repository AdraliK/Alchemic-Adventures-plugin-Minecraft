package listeners.customMobs;

import utils.ListenerRegister;
import utils.ListenerUtils;


public class MobListeners implements ListenerRegister {
    @Override
    public void register() {
        ListenerUtils.registerListeners(
                new MobsNewMechanics(),
                new SpiderWebShooter(),
                new AmethystSkeleton(),
                new TropicSkeleton()
        );
    }
}
