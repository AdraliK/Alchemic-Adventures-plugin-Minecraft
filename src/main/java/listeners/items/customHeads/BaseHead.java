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
    protected final String displayName;
    protected final List<String> lore;
    protected final String base64;
    protected HeadType headType;
    protected double armorValue = 0;

    public BaseHead(String displayName, List<String> lore, String base64) {
        this.displayName = displayName;
        this.lore = lore;
        this.base64 = base64;
    }

    public void setHeadType(HeadType headType) {
        this.headType = headType;
    }

    public ItemStack createItem() {
        ItemStack item = SkullCreator.itemFromBase64(base64);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(displayName);
        meta.setLore(lore);

        if (headType != null) {
            meta.getPersistentDataContainer().set(
                    new NamespacedKey("vanillaplus", "head_type"),
                    PersistentDataType.STRING,
                    headType.name()
            );
        }

        if (armorValue > 0) {
            addArmorAttribute(meta, armorValue);
        }

        item.setItemMeta(meta);
        return item;
    }

    private void addArmorAttribute(ItemMeta meta, double armorValue) {
        AttributeModifier armorModifier = new AttributeModifier(
                UUID.randomUUID(),
                "armor",
                armorValue,
                AttributeModifier.Operation.ADD_NUMBER
        );
        meta.addAttributeModifier(Attribute.ARMOR, armorModifier);
    }

    protected void setArmor(double armor) {
        this.armorValue = armor;
    }

    public String getDisplayName() { return displayName; }
    public List<String> getLore() { return lore; }
    public String getBase64() { return base64; }
    public double getArmorValue() { return armorValue; }
}