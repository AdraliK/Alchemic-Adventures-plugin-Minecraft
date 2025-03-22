package listeners.items;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.List;

public class CustomHeadDrop implements Listener {

    private final NamespacedKey key = new NamespacedKey(Main.javaPlugin, "head_lore");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (isValidHead(item)) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if (meta.hasLore()) {
                String lore = String.join("|", meta.getLore());
                data.set(key, PersistentDataType.STRING, lore);
            }

            Block block = event.getBlockPlaced();
            if (block.getState() instanceof Skull skull) {
                skull.getPersistentDataContainer().set(key, PersistentDataType.STRING, data.get(key, PersistentDataType.STRING));
                skull.update();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof Skull skull)) return;
        PersistentDataContainer data = skull.getPersistentDataContainer();
        if (!data.has(key, PersistentDataType.STRING)) return;

        String lore = data.get(key, PersistentDataType.STRING);
        Collection<ItemStack> drops = block.getDrops();
        List<ItemStack> modifiedDrops = drops.stream().map(drop -> {
            if (drop.getType() == Material.PLAYER_HEAD) {
                ItemMeta meta = drop.getItemMeta();
                meta.setLore(List.of(lore.split("\\|")));
                drop.setItemMeta(meta);
            }
            return drop;
        }).toList();

        event.setDropItems(false);
        modifiedDrops.forEach(drop -> block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), drop));
    }

    private boolean isValidHead(ItemStack item) {
        return item.getType() == Material.PLAYER_HEAD && item.hasItemMeta();
    }
}
