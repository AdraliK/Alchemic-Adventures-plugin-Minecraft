package listeners.items;

import listeners.items.customHeads.HeadType;
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

import java.util.List;

public class CustomHeadDrop implements Listener {

    private final NamespacedKey headTypeKey = new NamespacedKey("vanillaplus", "head_type");
    private final NamespacedKey displayNameKey = new NamespacedKey("vanillaplus", "display_name");
    private final NamespacedKey loreKey = new NamespacedKey("vanillaplus", "head_lore");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!isValidHead(item)) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        Block block = event.getBlockPlaced();
        if (block.getState() instanceof Skull skull) {
            copyMetaToBlock(meta, skull);
            skull.update();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof Skull skull)) return;

        PersistentDataContainer blockData = skull.getPersistentDataContainer();
        if (!blockData.has(headTypeKey, PersistentDataType.STRING)) return;

        HeadType headType = getHeadTypeFromBlock(blockData);
        if (headType == null) return;

        ItemStack drop = headType.getHeadItem();
        ItemMeta meta = drop.getItemMeta();
        if (meta == null) return;

        copyMetaFromBlock(blockData, meta);
        drop.setItemMeta(meta);

        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), drop);
    }

    private void copyMetaToBlock(ItemMeta itemMeta, Skull skull) {
        PersistentDataContainer blockData = skull.getPersistentDataContainer();
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

        copyPersistentKeysToBlock(itemData, blockData);
        copyDisplayNameToBlock(itemMeta, blockData);
        copyHeadTypeToBlock(itemData, blockData);
        copyLoreToBlock(itemMeta, blockData);
    }

    private void copyMetaFromBlock(PersistentDataContainer blockData, ItemMeta itemMeta) {
        PersistentDataContainer itemData = itemMeta.getPersistentDataContainer();

        copyPersistentKeysFromBlock(blockData, itemData);
        copyDisplayNameFromBlock(blockData, itemMeta);
        copyHeadTypeFromBlock(blockData, itemData);
        copyLoreFromBlock(blockData, itemMeta);
    }

    private void copyPersistentKeysToBlock(PersistentDataContainer source, PersistentDataContainer target) {
        for (NamespacedKey key : source.getKeys()) {
            Object value = source.get(key, PersistentDataType.STRING);
            if (value instanceof String stringValue) {
                target.set(key, PersistentDataType.STRING, stringValue);
            }
        }
    }

    private void copyPersistentKeysFromBlock(PersistentDataContainer source, PersistentDataContainer target) {
        for (NamespacedKey key : source.getKeys()) {
            Object value = source.get(key, PersistentDataType.STRING);
            if (value instanceof String stringValue) {
                target.set(key, PersistentDataType.STRING, stringValue);
            }
        }
    }

    private void copyDisplayNameToBlock(ItemMeta itemMeta, PersistentDataContainer blockData) {
        String displayName = itemMeta.getDisplayName();
        if (displayName != null && !displayName.isEmpty()) {
            blockData.set(displayNameKey, PersistentDataType.STRING, displayName);
        }
    }

    private void copyDisplayNameFromBlock(PersistentDataContainer blockData, ItemMeta itemMeta) {
        if (blockData.has(displayNameKey, PersistentDataType.STRING)) {
            String displayName = blockData.get(displayNameKey, PersistentDataType.STRING);
            itemMeta.setDisplayName(displayName);
        }
    }

    private void copyHeadTypeToBlock(PersistentDataContainer itemData, PersistentDataContainer blockData) {
        if (itemData.has(headTypeKey, PersistentDataType.STRING)) {
            String headType = itemData.get(headTypeKey, PersistentDataType.STRING);
            blockData.set(headTypeKey, PersistentDataType.STRING, headType);
        }
    }

    private void copyHeadTypeFromBlock(PersistentDataContainer blockData, PersistentDataContainer itemData) {
        if (blockData.has(headTypeKey, PersistentDataType.STRING)) {
            String headType = blockData.get(headTypeKey, PersistentDataType.STRING);
            itemData.set(headTypeKey, PersistentDataType.STRING, headType);
        }
    }

    private void copyLoreToBlock(ItemMeta itemMeta, PersistentDataContainer blockData) {
        if (itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            blockData.set(loreKey, PersistentDataType.STRING, String.join("\n", lore));
        }
    }

    private void copyLoreFromBlock(PersistentDataContainer blockData, ItemMeta itemMeta) {
        if (blockData.has(loreKey, PersistentDataType.STRING)) {
            String loreString = blockData.get(loreKey, PersistentDataType.STRING);
            if (loreString != null) {
                List<String> lore = List.of(loreString.split("\n"));
                itemMeta.setLore(lore);
            }
        }
    }

    private HeadType getHeadTypeFromBlock(PersistentDataContainer blockData) {
        String headTypeName = blockData.get(headTypeKey, PersistentDataType.STRING);
        try {
            return HeadType.valueOf(headTypeName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean isValidHead(ItemStack item) {
        return item.getType() == Material.PLAYER_HEAD && item.hasItemMeta();
    }
}