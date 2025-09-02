package listeners.blocks;

import utils.ListenerRegister;
import utils.ListenerUtils;

public class BlockListeners implements ListenerRegister {
    @Override
    public void register() {
        ListenerUtils.registerListeners(
                new CauldronInteract(),
                new ComposterInteract(),
                new AnvilInteract(),
                new StonecutterInteract(),
                new WaxedBlockInteract(),
                new EnchantingTableInteract()
        );
    }
}
