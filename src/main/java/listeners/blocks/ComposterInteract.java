package listeners.blocks;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ComposterInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.COMPOSTER) return;

        Player player = event.getPlayer();

        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

        ItemStack item = (itemInMainHand.getType() == Material.POISONOUS_POTATO ||
                itemInMainHand.getType() == Material.ROTTEN_FLESH) ?
                itemInMainHand : (itemInOffHand.getType() == Material.POISONOUS_POTATO ||
                itemInOffHand.getType() == Material.ROTTEN_FLESH) ?
                itemInOffHand : null;

        if (item == null) return;

        Levelled composterData = (Levelled) block.getBlockData();
        int currentLevel = composterData.getLevel();

        if (addLevelWithChance(currentLevel, composterData, block, item)) return;

        player.swingMainHand();
    }


    @EventHandler
    public void onItemMoveToComposter(InventoryMoveItemEvent event) {
        BlockState state = event.getDestination().getLocation().getBlock().getState();

        // Проверяем, если целевой блок - это компостер
        if (state.getType() != Material.COMPOSTER) return;

        Block composterBlock = state.getBlock();
        Levelled composterData = (Levelled) composterBlock.getBlockData();
        int currentLevel = composterData.getLevel();


        ItemStack item = event.getItem();

        // Проверяем, если предмет - это гнилая картошка или гнилая плоть
        if (item.getType() != Material.POISONOUS_POTATO && item.getType() != Material.ROTTEN_FLESH) return;

        // Проверяем, если компостер не полон
        if (addLevelWithChance(currentLevel, composterData, composterBlock, item)) return;

        event.setItem(item);  // Обновляем состояние предмета
    }

    private boolean addLevelWithChance(int currentLevel, Levelled composterData, Block block, ItemStack item) {
        if (currentLevel >= composterData.getMaximumLevel()) return true;

        double chance = Math.random();
        if (chance <= 0.75) {
            composterData.setLevel(currentLevel + 1);
            block.setBlockData(composterData);

            block.getWorld().playSound(block.getLocation().add(0.5, 1.5, 0.5),
                    Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 1.0f, 1.0f);

            block.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, block.getLocation()
                    .add(0.5, 0.7, 0.5), 5, 0.15, 0.15, 0.15, 1);

        } else {
            block.getWorld().playSound(block.getLocation().add(0.5, 1.5, 0.5),
                    Sound.BLOCK_COMPOSTER_FILL, 1.0f, 1.0f);

            block.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, block.getLocation()
                    .add(0.5, 0.7, 0.5), 5, 0.15, 0.15, 0.15, 1);

        }
        item.setAmount(item.getAmount() - 1);
        return false;
    }
}
