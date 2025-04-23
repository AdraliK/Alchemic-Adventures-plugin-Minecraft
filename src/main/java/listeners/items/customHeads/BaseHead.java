package listeners.items.customHeads;

import helpers.SkullCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.List;
import java.util.UUID;


public abstract class BaseHead {
    protected final ItemStack item;

    public BaseHead(String displayName, List<String> lore, String base64) {
        item = SkullCreator.itemFromBase64(base64);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    public void setHeadType(HeadType headType) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(
                new NamespacedKey("vanillaplus", "head_type"),
                PersistentDataType.STRING,
                headType.name()
        );
        item.setItemMeta(meta);
    }

    public void addArmorAttribute(double amount) {
        AttributeModifier armorModifier = new AttributeModifier(UUID.randomUUID(), "armor", amount, AttributeModifier.Operation.ADD_NUMBER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addAttributeModifier(Attribute.ARMOR, armorModifier);
        item.setItemMeta(itemMeta);
    }

    public ItemStack getItem() {
        return item.clone();
    }
}

