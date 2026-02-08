package listeners.items.customHeads;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CustomHead {

    private CustomHead() {

    }

    public static ItemStack createHead(HeadType headType) {
        return headType.getBaseHead().createItem();
    }

    public static boolean is(ItemStack item) {
        return getHeadTypeName(item) != null;
    }

    public static boolean typeIs(ItemStack item, HeadType expectedType) {
        if (item == null || item.getType() != Material.PLAYER_HEAD || !item.hasItemMeta()) {
            return false;
        }

        String headTypeName = getHeadTypeName(item);
        return expectedType != null && expectedType.name().equals(headTypeName);
    }

    public static String getHeadTypeName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey("vanillaplus", "head_type");
        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static HeadType getHeadType(ItemStack item) {
        String typeName = getHeadTypeName(item);
        if (typeName == null) return null;

        try {
            return HeadType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}