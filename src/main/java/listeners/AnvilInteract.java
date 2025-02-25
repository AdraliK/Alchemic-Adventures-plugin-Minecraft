package listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().isSneaking())) return;
        if (e.getClickedBlock() == null || e.getItem() == null) return;

        Block block = e.getClickedBlock();
        ItemStack itemInHand = e.getItem();

        if (block.getType() == Material.CHIPPED_ANVIL || block.getType() == Material.DAMAGED_ANVIL) {

            if (itemInHand.getType() != Material.IRON_INGOT) return;
            if (itemInHand.getAmount() < 3) {
                e.getPlayer().sendActionBar("Вам нужно как минимум 3 железных слитка, чтобы починить наковальню!");
                e.setCancelled(true);
                return;
            }

            itemInHand.setAmount(itemInHand.getAmount() - 3);

            BlockData originalData = block.getBlockData();

            if (originalData instanceof Directional directional) {

                switch (block.getType()) {
                    case DAMAGED_ANVIL:
                        block.setType(Material.CHIPPED_ANVIL);
                        break;
                    case CHIPPED_ANVIL:
                        block.setType(Material.ANVIL);
                        break;
                    default:
                        return;
                }

                BlockData newData = block.getBlockData();
                if (newData instanceof Directional) {
                    ((Directional) newData).setFacing(directional.getFacing());
                    block.setBlockData(newData);
                }
                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 2.0f);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerLookAtAnvil(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Block targetBlock = player.getTargetBlockExact(5);

        if (event.isSneaking() &&
                targetBlock != null &&
                targetBlock.getType().name().contains("_ANVIL") &&
                player.getInventory().getItemInMainHand().getType() == Material.IRON_INGOT) {

            player.sendActionBar("Нажмите ПКМ, чтобы починить наковальню");
        }
    }
}