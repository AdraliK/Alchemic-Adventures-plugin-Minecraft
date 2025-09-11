package recipes;

import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import utils.Loadable;

public class ChangeHeadRecipe implements Loadable {

    @Override
    public void register() {
        ItemStack result = CustomHead.createHead(HeadType.SKIN_HEAD);

        NamespacedKey key = new NamespacedKey("vanillaplus", "change_head_recipe");
        ShapelessRecipe recipe = new ShapelessRecipe(key, result);

        recipe.addIngredient(Material.PLAYER_HEAD);

        Bukkit.addRecipe(recipe);
    }

    @Override
    public void shutdown() {

    }
}
