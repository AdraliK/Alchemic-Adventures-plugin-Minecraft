package listeners.items.customHeads;

import org.bukkit.block.Skull;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public interface HeadHandler {

    default void onPlace(BlockPlaceEvent event, Skull skull, ItemStack item) {}
    default void onBreak(BlockBreakEvent event, Skull skull, ItemStack drop) {}

}
