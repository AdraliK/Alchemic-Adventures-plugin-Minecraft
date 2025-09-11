package listeners.items.customHeads;

import listeners.items.customHeads.fullMoonHeads.LeakyBag;
import listeners.items.customHeads.fullMoonHeads.PieceCheese;
import listeners.items.customHeads.fullMoonHeads.RadiantHelmet;
import listeners.items.customHeads.otherHeads.skinHead.SkinHeadListener;
import listeners.items.customHeads.otherHeads.minerHelmet.MinerHelmetListener;
import listeners.items.customHeads.otherHeads.undergroundDepthHelmet.UndergroundDepthHelmetListener;
import utils.ListenerRegister;
import utils.ListenerUtils;

public class CustomHeadsListeners implements ListenerRegister {
    @Override
    public void register() {
        ListenerUtils.registerListeners(
                new CustomHeadDrop(),
                new PieceCheese(),
                new RadiantHelmet(),
                new LeakyBag(),
                new SkinHeadListener(),
                new MinerHelmetListener(),
                new UndergroundDepthHelmetListener()
        );
    }
}
