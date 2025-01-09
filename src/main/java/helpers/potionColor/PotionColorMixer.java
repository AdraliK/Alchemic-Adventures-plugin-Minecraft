package helpers.potionColor;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;
import java.util.Map;

import static helpers.potionColor.PotionColorMap.potionColors;

public class PotionColorMixer {

    // Метод для смешивания цветов зелий
    public static Color mixPotionColors(List<ItemStack> potions) {
        int totalRed = 0, totalGreen = 0, totalBlue = 0;
        int validPotionCount = 0;

        for (ItemStack potion : potions) {
            if (potion.getItemMeta() instanceof PotionMeta) {
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                String potionType = potionMeta.getBasePotionData().getType().name();

                // Получаем цвет зелья из карты
                Color color = potionColors.get(potionType);
                if (color != null) {
                    totalRed += color.getRed();
                    totalGreen += color.getGreen();
                    totalBlue += color.getBlue();
                    validPotionCount++;
                }
            }
        }

        if (validPotionCount == 0) {
            return Color.WHITE; // Если нет допустимых зелий, возвращаем белый цвет
        }

        // Вычисляем средний цвет
        int avgRed = totalRed / validPotionCount;
        int avgGreen = totalGreen / validPotionCount;
        int avgBlue = totalBlue / validPotionCount;

        return Color.fromARGB(255, avgRed, avgGreen, avgBlue);
    }
}

