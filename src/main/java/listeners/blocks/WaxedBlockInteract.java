package listeners.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class WaxedBlockInteract implements Listener {

    @EventHandler
    public void onWaxedBlockClicked(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        Block block = event.getClickedBlock();
        Material blockType = block.getType();

        if (!blockType.name().toLowerCase().startsWith("waxed_")) return;

        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        if (!(isAxe(mainHand) || isAxe(offHand))) return;

        Location location = block.getLocation().add(0.5, 1.2, 0.5);
        block.getWorld().dropItemNaturally(location, new ItemStack(Material.HONEYCOMB));
    }

    private boolean isAxe(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        return item.getType().name().toLowerCase().endsWith("_axe");
    }

}
