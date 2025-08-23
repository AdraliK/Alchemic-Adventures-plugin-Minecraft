package listeners.blocks;

import helpers.DatapackUtils;
import helpers.HintsManager;
import helpers.Updater;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilInteract implements Listener {

    public AnvilInteract() {
        Updater.addTask(this::onPlayerLookAtAnvil);
    }

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
                DatapackUtils.grantAdvancement(e.getPlayer(), "fix_anvil");
                e.setCancelled(true);
            }
        }
    }

    private void onPlayerLookAtAnvil(Player player) {
        if (!HintsManager.hasHints(player.getUniqueId())) return;

        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock != null && targetBlock.getType().name().contains("_ANVIL")) {
            player.sendActionBar("Нажмите Shift + ПКМ, используя железные слитки для починки наковальни");
        }
    }

    @EventHandler
    public void onAnvilRename(PrepareAnvilEvent event) {
        ItemStack result = event.getResult();
        if (result == null || !result.hasItemMeta()) return;

        ItemMeta meta = result.getItemMeta();
        String displayName = meta.getDisplayName();
        if (displayName.isEmpty()) return;

        Component newName = Component.text(displayName).decoration(TextDecoration.ITALIC, false);
        meta.displayName(newName);

        result.setItemMeta(meta);
        event.getInventory().setResult(result);
    }
}