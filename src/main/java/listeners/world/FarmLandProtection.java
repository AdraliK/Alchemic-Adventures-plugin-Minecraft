package listeners.world;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class FarmLandProtection implements Listener {


    @EventHandler
    public void onPlayerStepOnFarmland(EntityChangeBlockEvent event) {
        // Проверяем, если сущность - игрок и блок - грядка
        if (!(event.getEntity() instanceof LivingEntity)) return;

        if (event.getBlock().getType() == Material.FARMLAND) {
            Block block = event.getBlock();
            Farmland farmland = (Farmland) block.getBlockData();

            // Проверяем, увлажнена ли грядка
            if (farmland.getMoisture() > 0) {
                Block aboveBlock = block.getRelative(0, 1, 0);

                // Проверяем, если над грядкой растет растение
                if (aboveBlock.getBlockData() instanceof Ageable) {
                    event.setCancelled(true); // Отменяем изменение блока (не позволяем сломать грядку)
                }
            }
        }
    }

}
