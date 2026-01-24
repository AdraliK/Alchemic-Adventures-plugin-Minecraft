package listeners.items.customHeads.otherHeads.skinHead;

import listeners.items.customHeads.HeadHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Skull;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class SkinHeadHandler implements HeadHandler {

    private static final NamespacedKey TEXTURE_VALUE_KEY = new NamespacedKey("vanillaplus", "skin_texture_value");

    @Override
    public void onBreak(BlockBreakEvent event, Skull skull, ItemStack drop) {
        SkullMeta meta = (SkullMeta) drop.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();

        String skinURLString = container.get(TEXTURE_VALUE_KEY, PersistentDataType.STRING);
        if (skinURLString != null) {
            try {
                URL skinURL = new URL(skinURLString);
                PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
                profile.getTextures().setSkin(skinURL);
                meta.setOwnerProfile(profile);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        drop.setItemMeta(meta);
    }

}
