package listeners.items.customHeads.otherHeads.skinHead;

import listeners.items.customHeads.HeadHandler;
import org.bukkit.block.Skull;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;

import java.util.UUID;

public class SkinHeadHandler implements HeadHandler {

    @Override
    public void onBreak(BlockBreakEvent event, Skull skull, ItemStack drop) {
        PersistentDataContainer blockData = skull.getPersistentDataContainer();
        if (!blockData.has(SkinHeadListener.getPlayerUuidKey(), PersistentDataType.STRING)) return;

        String uuidString = blockData.get(SkinHeadListener.getPlayerUuidKey(), PersistentDataType.STRING);
        UUID uuid = java.util.UUID.fromString(uuidString);

        PlayerProfile profile = org.bukkit.Bukkit.createPlayerProfile(uuid);
        SkullMeta meta = (SkullMeta) drop.getItemMeta();
        meta.setOwnerProfile(profile);

        drop.setItemMeta(meta);
    }

}
