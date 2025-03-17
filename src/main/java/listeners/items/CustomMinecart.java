package listeners.items;

import helpers.DatapackUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CustomMinecart implements Listener {

    private final Set<UUID> customMinecarts = new HashSet<>();

    @EventHandler
    public void onShiftRightClickRail(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (event.getAction().toString().contains("RIGHT_CLICK") &&
                player.isSneaking() &&
                clickedBlock != null &&
                clickedBlock.getType().name().contains("RAIL") &&
                player.getInventory().getItemInMainHand().getType() == Material.AIR) {

            Location spawnLoc = clickedBlock.getLocation().add(0.5, 0.1, 0.5);
            Minecart minecart = player.getWorld().spawn(spawnLoc, Minecart.class);
            customMinecarts.add(minecart.getUniqueId());

            player.setSneaking(false);
            minecart.addPassenger(player);

            DatapackUtils.grantAdvancement(player, "new_minecart");

            Vector direction = player.getLocation().getDirection().setY(0).normalize().multiply(0.2);
            minecart.setVelocity(direction);
        }
    }

    @EventHandler
    public void onPlayerExitMinecart(VehicleExitEvent event) {
        if (!(event.getVehicle() instanceof Minecart minecart)) return;
        if (!(event.getExited() instanceof Player player)) return;
        if (!customMinecarts.contains(minecart.getUniqueId())) return;

        player.teleport(player.getLocation().add(0, 0.5, 0));
        customMinecarts.remove(minecart.getUniqueId());
        minecart.remove();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!player.isInsideVehicle()) return;
        if (!(player.getVehicle() instanceof Minecart minecart)) return;
        if (!customMinecarts.contains(minecart.getUniqueId())) return;

        customMinecarts.remove(minecart.getUniqueId());
        minecart.remove();
    }

    @EventHandler
    public void onMinecartDestroy(VehicleDestroyEvent event) {
        if (!(event.getVehicle() instanceof Minecart minecart)) return;
        if (!customMinecarts.contains(minecart.getUniqueId())) return;

        event.setCancelled(true);
        minecart.remove();
    }

    @EventHandler
    public void onPlayerLookAtRail(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Block targetBlock = player.getTargetBlockExact(5);

        if (event.isSneaking() &&
                targetBlock != null &&
                targetBlock.getType().name().contains("RAIL") &&
                player.getInventory().getItemInMainHand().getType() == Material.AIR) {

            player.sendActionBar("Нажмите ПКМ, чтобы призвать вагонетку");
        }
    }
}
