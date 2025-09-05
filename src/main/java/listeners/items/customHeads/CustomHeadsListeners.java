package listeners.items.customHeads;

import listeners.items.customHeads.fullMoonHeads.LeakyBag;
import listeners.items.customHeads.fullMoonHeads.PieceCheese;
import listeners.items.customHeads.fullMoonHeads.RadiantHelmet;
import utils.ListenerRegister;
import utils.ListenerUtils;

public class CustomHeadsListeners implements ListenerRegister {
    @Override
    public void register() {
        ListenerUtils.registerListeners(
                new PieceCheese(),
                new RadiantHelmet(),
                new LeakyBag()
        );
    }
}
