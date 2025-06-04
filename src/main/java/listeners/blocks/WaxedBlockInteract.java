package listeners.blocks;

import org.bukkit.Bukkit;
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

import static adralik.vanillaPlus.Main.javaPlugin;

public class WaxedBlockInteract implements Listener {

    @EventHandler
    public void onWaxedBlockInteract(PlayerInteractEvent event) {
        if (!shouldProcessEvent(event)) {
            return;
        }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        Location blockLocation = block.getLocation();
        Material originalMaterial = block.getType();

        scheduleWaxRemovalCheck(player, blockLocation, originalMaterial);
    }

    private boolean shouldProcessEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return false;
        }
        String blockName = event.getClickedBlock().getType().name().toLowerCase();
        if (!blockName.startsWith("waxed_")) {
            return false;
        }
        ItemStack handItem = getHandItem(event);
        return isAxe(handItem);
    }

    private ItemStack getHandItem(PlayerInteractEvent event) {
        EquipmentSlot hand = event.getHand();
        Player player = event.getPlayer();

        return hand == EquipmentSlot.HAND ?
                player.getInventory().getItemInMainHand() :
                player.getInventory().getItemInOffHand();
    }

    private void scheduleWaxRemovalCheck(Player player, Location blockLocation, Material originalMaterial) {
        Bukkit.getScheduler().runTaskLater(javaPlugin, () -> {
            Block currentBlock = blockLocation.getBlock();

            if (!isValidBlockChange(currentBlock, originalMaterial)) {
                return;
            }

            dropHoneycomb(blockLocation);
        }, 1);
    }

    private boolean isValidBlockChange(Block currentBlock, Material originalMaterial) {
        if (currentBlock.getType() == Material.AIR) {
            return false;
        }

        String originalName = originalMaterial.name().toLowerCase();
        String currentName = currentBlock.getType().name().toLowerCase();

        return currentName.equals(originalName.substring(6)) &&
                !currentName.startsWith("waxed_");
    }

    private void dropHoneycomb(Location location) {
        location.getWorld().dropItemNaturally(
                location.add(0.5, 1.2, 0.5),
                new ItemStack(Material.HONEYCOMB)
        );
    }

    private boolean isAxe(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        return item.getType().name().toLowerCase().endsWith("_axe");
    }
}
