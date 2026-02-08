package listeners.items.customHeads.otherHeads.skinHead;

import listeners.items.customHeads.CustomHead;
import listeners.items.customHeads.HeadType;
import org.bukkit.Material;
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

import java.net.URL;
import java.util.List;

public class SkinHeadListener implements Listener {

    private static final NamespacedKey TEXTURE_VALUE_KEY = new NamespacedKey("vanillaplus", "skin_texture_value");

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
        if (meta == null) return;

        setPlayerSkin(meta, player);
        setItemNameAndLore(meta, player);

        item.setItemMeta(meta);
    }

    private void setPlayerSkin(SkullMeta meta, Player player) {
        PlayerProfile profile = player.getPlayerProfile();
        meta.setOwnerProfile(profile);

        URL skinURL = profile.getTextures().getSkin();
        if (skinURL != null) {
            String value = skinURL.toString();
            meta.getPersistentDataContainer().set(TEXTURE_VALUE_KEY, PersistentDataType.STRING, value);
        }
    }

    private void setItemNameAndLore(SkullMeta meta, Player player) {
        meta.setDisplayName("§eГолова " + player.getName());
        meta.setLore(
                List.of(
                        "",
                        "§7Можно получить заготовку,",
                        "§7поместив в верстак"
                )
        );
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();

        ItemStack result = inv.getResult();
        if (result == null || !CustomHead.typeIs(result, HeadType.SKIN_HEAD)) {
            return;
        }

        for (ItemStack item : inv.getMatrix()) {
            if (item != null && item.getType() == Material.PLAYER_HEAD) {
                if (!hasSkin(item)) {
                    inv.setResult(null);
                    return;
                }
                break;
            }
        }
    }

    private boolean hasSkin(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(TEXTURE_VALUE_KEY, PersistentDataType.STRING);
    }

}
