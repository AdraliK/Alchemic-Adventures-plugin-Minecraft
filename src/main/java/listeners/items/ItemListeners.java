package listeners.items;

import listeners.items.customHeads.CustomHeadsListeners;
import utils.ListenerRegister;
import utils.ListenerUtils;

public class ItemListeners implements ListenerRegister {
    @Override
    public void register() {
        ListenerUtils.registerListeners(
                new SoupFix(),
                new TotemCooldown(),
                new CustomMinecart(),
                new CustomHeadDrop()
        );
    }
}
