package listeners.customMobs;

import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

import static adralik.vanillaPlus.Main.javaPlugin;

public class CustomWanderingTrades implements Listener {

    private static final int ITEM_TRADE_COUNT = 8;

    @EventHandler
    public void onTraderSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getType() != EntityType.WANDERING_TRADER) return;

        WanderingTrader trader = (WanderingTrader) event.getEntity();

        Bukkit.getScheduler().runTaskLater(javaPlugin, () -> {
            List<MerchantRecipe> recipes = new ArrayList<>(trader.getRecipes());

            ItemStack result = CustomHead.createHead(HeadType.SKIN_HEAD);
            ItemStack price = new ItemStack(Material.AMETHYST_SHARD, ITEM_TRADE_COUNT);

            MerchantRecipe customRecipe = new MerchantRecipe(
                    result,
                    0,
                    9999,
                    true,
                    0,
                    0.0f
            );
            customRecipe.addIngredient(price);

            recipes.add(customRecipe);
            trader.setRecipes(recipes);
        }, 1L);
    }
}
