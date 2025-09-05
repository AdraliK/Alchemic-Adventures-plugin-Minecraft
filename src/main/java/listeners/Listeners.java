package listeners;

import customMobs.MobListeners;
import listeners.blocks.BlockListeners;
import listeners.items.ItemListeners;
import listeners.items.customHeads.CustomHeadsListeners;
import listeners.world.WorldListeners;
import utils.ListenerRegister;
import utils.Loadable;

import java.util.List;

public class Listeners implements Loadable {

    @Override
    public void register() {
        List<ListenerRegister> listenerGroups = List.of(
                new WorldListeners(),
                new MobListeners(),
                new BlockListeners(),
                new ItemListeners(),
                new CustomHeadsListeners()
        );
        listenerGroups.forEach(ListenerRegister::register);
    }

    @Override
    public void shutdown() {

    }
}
