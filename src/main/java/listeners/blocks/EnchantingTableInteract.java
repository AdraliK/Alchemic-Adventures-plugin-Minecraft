package listeners.blocks;

import helpers.DatapackUtils;
import helpers.HintsManager;
import helpers.HintsUpdater;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import utils.ActionBarLock;

import java.util.Random;

public class EnchantingTableInteract implements Listener {

    private final Random random = new Random();
    private static final int lapisCountTrade = 16;

    public EnchantingTableInteract() {
        HintsUpdater.addTask(this::onPlayerLookAtEnchantingTable);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().isSneaking())) return;
        if (e.getClickedBlock() == null || e.getItem() == null) return;

        Block block = e.getClickedBlock();
        ItemStack itemInHand = e.getItem();

        if (block.getType() != Material.ENCHANTING_TABLE) return;

        if (itemInHand.getType() != Material.LAPIS_LAZULI) return;
        if (itemInHand.getAmount() < lapisCountTrade) {
            e.getPlayer().sendActionBar("Вам нужно как минимум " + lapisCountTrade + " лазурита!");
            ActionBarLock.lock(e.getPlayer(), 3000);
            e.setCancelled(true);
            return;
        }

        itemInHand.setAmount(itemInHand.getAmount() - lapisCountTrade);
        e.getPlayer().setEnchantmentSeed(random.nextInt());

        playEffects(block);

        DatapackUtils.grantAdvancement(e.getPlayer(), "use_enchant_table");
    }

    private void playEffects(Block block) {
        block.getWorld().playSound(block.getLocation(),
                Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.5f);

        block.getWorld().spawnParticle(
                Particle.DUST,
                block.getLocation().add(0.5, 1, 0.5),
                25,
                0.6, 0.6, 0.6,
                new Particle.DustOptions(Color.fromARGB(1, 0, 145, 255), 1f)
        );
        block.getWorld().spawnParticle(
                Particle.ENCHANT,
                block.getLocation().add(0.5, 1, 0.5),
                35,
                0.35, 0.35, 0.35,
                0.8
        );
    }

    private void onPlayerLookAtEnchantingTable(Player player) {
        if (!HintsManager.hasHints(player.getUniqueId())) return;

        Block targetBlock = player.getTargetBlockExact(5);
        if (targetBlock != null && targetBlock.getType() == Material.ENCHANTING_TABLE) {
            player.sendActionBar("Нажмите Shift + ПКМ, используя лазурит для обновления зачарований");
        }
    }

}
