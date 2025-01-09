package helpers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class RandomLoot {

    private final double chance;
    private final ItemStack itemStack;
    private final Random random = new Random();

    public RandomLoot(Material material, int minAmount, int maxAmount, double chance) {
        this.chance = chance;
        itemStack = new ItemStack(material, getAmount(minAmount, maxAmount));
    }

    public RandomLoot(Material material, double chance) {
        this.chance = chance;
        itemStack = new ItemStack(material, 1);
    }

    public RandomLoot(String name, String lore, String base64, double chance) {
        this.chance = chance;
        itemStack = SkullCreator.itemFromBase64(base64);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(List.of(lore));
        itemStack.setItemMeta(itemMeta);
    }

    private int getAmount(int minAmount, int maxAmount){
        return random.nextInt(maxAmount - minAmount + 1) + minAmount;
    }

    public double getChance() {
        return chance;
    }

    public ItemStack getItem(){
        return itemStack;
    }
}

