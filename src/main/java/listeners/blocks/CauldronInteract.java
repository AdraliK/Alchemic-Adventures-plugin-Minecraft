package listeners.blocks;

import adralik.vanillaPlus.Main;
import helpers.CauldronData;
import helpers.DatapackUtils;
import helpers.potionColor.PotionColorMixer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CauldronInteract implements Listener {

    private List<CauldronData> cauldronDataList = new ArrayList<>();

    @EventHandler
    private void clickBlock(PlayerInteractEvent e) {
        if (!isValidInteraction(e)) return;

        Block block = e.getClickedBlock();
        ItemStack item = e.getItem();

        switch (item.getType()) {
            case POTION:
                handlePotionInteraction(e, block, item);
                break;
            case HONEY_BOTTLE:
                handleHoneyBottleInteraction(e, block);
                break;
            case GLASS_BOTTLE:
                handleGlassBottleInteraction(e, block);
                break;
            case WATER_BUCKET:
            case BUCKET:
                handleWaterBucketInteraction(e, block);
                break;
        }
    }

    private boolean isValidInteraction(PlayerInteractEvent e) {
        return e.getHand() == EquipmentSlot.HAND && !e.getAction().isLeftClick() &&
                e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.CAULDRON ||
                e.getClickedBlock().getType() == Material.WATER_CAULDRON) && e.getItem() != null;
    }

    private void handlePotionInteraction(PlayerInteractEvent e, Block block, ItemStack item) {
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        if (potionMeta == null || potionMeta.getBasePotionData() == null) {
            sendActionMessage(e.getPlayer(), "Интересный элексир нельзя добавлять в котел");
            return;
        }

        PotionType potionType = potionMeta.getBasePotionData().getType();
        if (isInvalidPotionType(potionType)) return;

        if (potionType == PotionType.WATER) {
            if (getCauldronDataIndex(block.getLocation()) != -1) {
                sendActionMessage(e.getPlayer(), "В котле находится таинственный раствор");
                e.setCancelled(true);
            }
            return;
        }

        if (block.getType() == Material.CAULDRON) {
            fillCauldronWithPotion(e, block, item);
        } else {
            increaseCauldronLevel(e, block, item);
        }

        e.getPlayer().swingMainHand();
    }

    private boolean isInvalidPotionType(PotionType potionType) {
        return potionType == PotionType.MUNDANE || potionType == PotionType.THICK || potionType == PotionType.AWKWARD;
    }

    private void fillCauldronWithPotion(PlayerInteractEvent e, Block block, ItemStack item) {
        block.setType(Material.WATER_CAULDRON);
        cauldronDataList.add(new CauldronData(block.getLocation(), item));
        addGlassBottleWithDelay(e);
        e.setCancelled(true);
        playSound(block, Sound.ITEM_BOTTLE_EMPTY);
        cauldronDataList.get(cauldronDataList.size() - 1).startParticleEffect(block);
    }

    private void increaseCauldronLevel(PlayerInteractEvent e, Block block, ItemStack item) {
        Levelled cauldronData = (Levelled) block.getBlockData();
        if (cauldronData.getLevel() < 3) {
            int index = getCauldronDataIndex(block.getLocation());
            if (!cauldronDataList.get(index).addPotion(item)) {
                sendActionMessage(e.getPlayer(), "Зелье такого типа уже налито");
                e.setCancelled(true);
                return;
            }

            addGlassBottleWithDelay(e);
            e.setCancelled(true);
            cauldronData.setLevel(cauldronData.getLevel() + 1);
            block.setBlockData(cauldronData);
            playSound(block, Sound.ITEM_BOTTLE_EMPTY);
        }
    }

    private void handleHoneyBottleInteraction(PlayerInteractEvent e, Block block) {
        if (block.getType() == Material.CAULDRON) return;

        Levelled cauldronData = (Levelled) block.getBlockData();
        if (cauldronData.getLevel() >= 2) {
            createCustomPotion(block.getLocation(), e.getPlayer());
            playSound(block, Sound.BLOCK_BREWING_STAND_BREW);
            resetCauldron(e, block);
        }

        DatapackUtils.grantAdvancement(e.getPlayer(), "cauldron_potion");
    }

    private void resetCauldron(PlayerInteractEvent e, Block block) {
        block.setType(Material.CAULDRON);
        e.setCancelled(true);
        adjustItemStack(e.getItem(), e.getPlayer());
        int index = getCauldronDataIndex(block.getLocation());
        if (index != -1) {
            cauldronDataList.get(index).stopParticleEffect();
            cauldronDataList.remove(index);
        }
    }

    private void handleGlassBottleInteraction(PlayerInteractEvent e, Block block) {
        int index = getCauldronDataIndex(block.getLocation());
        if (index == -1) return;

        Levelled cauldronData = (Levelled) block.getBlockData();
        givePlayerPotion(e, index);

        if (cauldronData.getLevel() > 1) {
            cauldronData.setLevel(cauldronData.getLevel() - 1);
            block.setBlockData(cauldronData);
        } else {
            block.setType(Material.CAULDRON);
            cauldronDataList.get(index).stopParticleEffect();
            cauldronDataList.remove(index);
        }

        e.setCancelled(true);
        playSound(block, Sound.ITEM_BOTTLE_FILL);
    }

    private void givePlayerPotion(PlayerInteractEvent e, int index) {
        List<ItemStack> potions = cauldronDataList.get(index).getPotionList();
        ItemStack potion = potions.get(potions.size() - 1);

        if (e.getItem().getAmount() > 1) {
            e.getPlayer().getInventory().addItem(potion);
            e.getItem().setAmount(e.getItem().getAmount() - 1);
        } else {
            e.getPlayer().setItemInHand(potion);
        }

        potions.remove(potions.size() - 1);
    }

    private void handleWaterBucketInteraction(PlayerInteractEvent e, Block block) {
        if (getCauldronDataIndex(block.getLocation()) != -1) {
            sendActionMessage(e.getPlayer(), "В котле находится таинственный раствор");
            e.setCancelled(true);
        }
    }

    private void sendActionMessage(Player player, String message) {
        player.sendActionBar(message);
    }

    private void playSound(Block block, Sound sound) {
        block.getWorld().playSound(block.getLocation(), sound, 1.0f, 1.0f);
    }

    private void adjustItemStack(ItemStack item, Player player) {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInHand(null);
        }
    }

    private int getCauldronDataIndex(Location location) {
        for (int i = 0; i < cauldronDataList.size(); i++) {
            if (cauldronDataList.get(i).getLocation().equals(location)) {
                return i;
            }
        }
        return -1;
    }

    private void createCustomPotion(Location location, Player player) {
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();

        int index = getCauldronDataIndex(location);
        for (int i = 0; i < cauldronDataList.get(index).getPotionList().size(); i++){
            meta.addCustomEffect(cauldronDataList.get(index).getPotionEffectFromList(i), true);
        }

        meta.setDisplayName("§bИнтересный элексир");
        Color mixColor = PotionColorMixer.mixPotionColors(cauldronDataList.get(index).getPotionList());
        meta.setColor(mixColor);
        item.setItemMeta(meta);

        player.getInventory().addItem(item);
    }

    private void addGlassBottleWithDelay(PlayerInteractEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.GLASS_BOTTLE, 1));
            }
        }.runTaskLater(Main.javaPlugin, 1);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.WATER_CAULDRON) {
            int index = getCauldronDataIndex(e.getBlock().getLocation());
            if (index != -1) {
                cauldronDataList.get(index).stopParticleEffect();
                cauldronDataList.remove(index);
            }
        }
    }
}
