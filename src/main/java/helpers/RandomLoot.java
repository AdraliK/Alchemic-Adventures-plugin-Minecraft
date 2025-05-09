package helpers;

import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class RandomLoot {
    
    private final int weight;
    private final ItemStack itemStack;
    private static final Random random = new Random();

    public RandomLoot(Material material, int minAmount, int maxAmount, int weight) {
        this.weight = weight;
        itemStack = new ItemStack(material, getAmount(minAmount, maxAmount));
    }

    public RandomLoot(Material material, int weight) {
        this.weight = weight;
        itemStack = new ItemStack(material, 1);
    }

    public RandomLoot(HeadType headType, int weight) {
        this.weight = weight;
        itemStack = new CustomHead(headType).getItem();
    }

    private int getAmount(int minAmount, int maxAmount){
        return random.nextInt(maxAmount - minAmount + 1) + minAmount;
    }

    public int getWeight() {
        return weight;
    }

    public ItemStack getItem(){
        return itemStack.clone();
    }

    public static ItemStack getRandomItem(List<RandomLoot> lootList) {
        if (lootList == null || lootList.isEmpty()) {
            return null;
        }

        int totalWeight = lootList.stream().mapToInt(RandomLoot::getWeight).sum();
        if (totalWeight <= 0) {
            return null;
        }

        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (RandomLoot loot : lootList) {
            currentWeight += loot.getWeight();
            if (randomValue < currentWeight) {
                return loot.getItem();
            }
        }

        return null;
    }
}

