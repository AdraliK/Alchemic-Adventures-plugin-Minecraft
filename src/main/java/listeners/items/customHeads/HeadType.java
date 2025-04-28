package listeners.items.customHeads;

import listeners.items.customHeads.fullMoonHeads.RadiantHelmet;
import listeners.items.customHeads.fullMoonHeads.LeakyBag;
import listeners.items.customHeads.fullMoonHeads.PieceCheese;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public enum HeadType {
    LEAKY_BAG(new LeakyBag()),
    PIECE_CHEESE(new PieceCheese()),
    RADIANT_HELMET(new RadiantHelmet());

    private final BaseHead baseHead;

    HeadType(BaseHead baseHead) {
        this.baseHead = baseHead;
        this.baseHead.setHeadType(this);
    }

    public ItemStack getHeadItem() {
        return baseHead.getItem();
    }

    public static boolean is(ItemStack item, HeadType expectedType) {
        String headTypeName = getHeadTypeName(item);
        return headTypeName != null && headTypeName.equals(expectedType.name());
    }

    private static String getHeadTypeName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        NamespacedKey key = new NamespacedKey("vanillaplus", "head_type");

        return meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }
}

