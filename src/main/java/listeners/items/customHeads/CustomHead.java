package listeners.items.customHeads;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CustomHead {
    private final ItemStack item;

    public CustomHead(HeadType headType) {
        item = headType.getHeadItem();
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public static boolean is(ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey("vanillaplus", "head_type");

        return meta.getPersistentDataContainer().has(key, PersistentDataType.STRING);
    }
}

