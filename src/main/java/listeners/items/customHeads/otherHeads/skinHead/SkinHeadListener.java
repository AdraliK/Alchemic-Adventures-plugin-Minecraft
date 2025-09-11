package listeners.items.customHeads.otherHeads.skinHead;

import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

public class SkinHeadListener implements Listener {

    private static final NamespacedKey PLAYER_UUID_KEY = new NamespacedKey("vanillaplus", "player_uuid");

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getAction().isRightClick() || !event.getPlayer().isSneaking()) return;

        ItemStack item = event.getItem();
        if (item == null) return;
        if (!CustomHead.typeIs(item, HeadType.SKIN_HEAD)) return;
        if (hasSkin(item)) return;

        event.setCancelled(true);
        Player player = event.getPlayer();

        applyPlayerSkin(item, player);
        player.getInventory().setItemInMainHand(item);
    }

    private void applyPlayerSkin(ItemStack item, Player player) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        setPlayerSkin(meta, player);
        setDataOfUuidPlayer(meta, player);
        meta.setDisplayName("§eГолова " + player.getName());

        item.setItemMeta(meta);
    }

    private void setPlayerSkin(SkullMeta meta, Player player) {
        PlayerProfile profile = player.getPlayerProfile();
        meta.setOwnerProfile(profile);
    }

    private void setDataOfUuidPlayer(SkullMeta meta, Player player) {
        meta.getPersistentDataContainer().set(PLAYER_UUID_KEY, PersistentDataType.STRING, player.getUniqueId().toString());
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();

        ItemStack result = inv.getResult();
        if (result == null) return;

        for (ItemStack item : inv.getMatrix()) {
            if (item == null || !item.hasItemMeta()) continue;
            if(hasSkin(item)) {
                return;
            }
        }

        inv.setResult(null);
    }

    private boolean hasSkin(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(PLAYER_UUID_KEY, PersistentDataType.STRING);
    }

    public static NamespacedKey getPlayerUuidKey() {
        return PLAYER_UUID_KEY;
    }

}
