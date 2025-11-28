package helpers;

import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class RandomLoot {

    private final int weight;
    private final Material material;
    private final HeadType headType;
    private final int minAmount;
    private final int maxAmount;
    private static final Random random = new Random();

    public RandomLoot(Material material, int minAmount, int maxAmount, int weight) {
        this.weight = weight;
        this.material = material;
        this.headType = null;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public RandomLoot(Material material, int weight) {
        this(material, 1, 1, weight);
    }

    public RandomLoot(HeadType headType, int weight) {
        this.weight = weight;
        this.material = null;
        this.headType = headType;
        this.minAmount = 1;
        this.maxAmount = 1;
    }

    private int getAmount(int minAmount, int maxAmount){
        return random.nextInt(maxAmount - minAmount + 1) + minAmount;
    }

    public int getWeight() {
        return weight;
    }

    public ItemStack getItem(){
        if (headType != null) {
            return CustomHead.createHead(headType);
        } else if (material != null) {
            return new ItemStack(material, getAmount(minAmount, maxAmount));
        }
        return null;
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