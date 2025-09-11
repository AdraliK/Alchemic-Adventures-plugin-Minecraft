package listeners.items.customHeads.fullMoonHeads;

import adralik.vanillaPlus.Main;
import helpers.DatapackUtils;
import listeners.items.customHeads.BaseHead;
import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PieceCheese extends BaseHead implements Listener {

    private static final int FOOD_BONUS = 3;

    public PieceCheese() {
        super(
                "§eОсколок сыра",
                List.of(
                        "§7ऑ Полнолуние",
                        "",
                        "§7Усиливает еду:",
                        "§9+3 к голоду и насыщению"
                        ),
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE1MzlkYmNkMzZmODc3MjYzMmU1NzM5ZTJlNTE0ODRlZGYzNzNjNTU4ZDZmYjJjNmI2MWI3MmI3Y2FhIn19fQ=="
        );
        setArmor(BASE_ARMOR_AMOUNT);
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (isNotNaturalFood(item.getType())) return;

        ItemStack helmet = player.getInventory().getHelmet();
        if (!CustomHead.typeIs(helmet, HeadType.PIECE_CHEESE)) return;

        DatapackUtils.grantAdvancement(player, "use_piececheese");

        Bukkit.getScheduler().runTask(Main.javaPlugin, () -> {
            int newFood = Math.min(player.getFoodLevel() + FOOD_BONUS, 20);
            float newSaturation = Math.min(player.getSaturation() + FOOD_BONUS, newFood);
            player.setFoodLevel(newFood);
            player.setSaturation(newSaturation);
        });
    }

    private boolean isNotNaturalFood(Material material) {
        return switch (material) {
            case POTION,
                 MILK_BUCKET,
                 SPLASH_POTION,
                 LINGERING_POTION,
                 HONEY_BOTTLE,
                 GOLDEN_APPLE,
                 ENCHANTED_GOLDEN_APPLE,
                 ROTTEN_FLESH,
                 SPIDER_EYE,
                 PUFFERFISH,
                 POISONOUS_POTATO -> true;
            default -> false;
        };
    }
}
