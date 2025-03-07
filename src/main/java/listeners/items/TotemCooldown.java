package listeners.items;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TotemCooldown implements Listener {

    private final Map<UUID, Long> totemCooldowns = new HashMap<>();
    private final long cooldownDuration = 60000; // 1 минуты в миллисекундах

    @EventHandler
    public void onTotemUse(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();

        if (player.hasCooldown(Material.TOTEM_OF_UNDYING)) {
            event.setCancelled(true);
            return;
        }

        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (mainHandItem.getType() == Material.TOTEM_OF_UNDYING || offHandItem.getType() == Material.TOTEM_OF_UNDYING) {
            UUID playerId = player.getUniqueId();
            long startTime = System.currentTimeMillis();
            totemCooldowns.put(playerId, startTime);

            player.setCooldown(Material.TOTEM_OF_UNDYING, (int) (cooldownDuration / 50));

            if (mainHandItem.getType() == Material.TOTEM_OF_UNDYING) {
                player.getInventory().setItemInMainHand(null);
            } else
            if (offHandItem.getType() == Material.TOTEM_OF_UNDYING) {
                player.getInventory().setItemInOffHand(null);
            }

            // Запускаем задачу по удалению записи из map после окончания кулдауна
            Bukkit.getScheduler().runTaskLater(Main.javaPlugin, () -> {
                totemCooldowns.remove(playerId);
            }, cooldownDuration / 50); // Переводим миллисекунды в тики
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (totemCooldowns.containsKey(playerId)) {
            long startTime = totemCooldowns.get(playerId);
            long elapsedTime = System.currentTimeMillis() - startTime;

            if (elapsedTime < cooldownDuration) {
                long remainingTime = cooldownDuration - elapsedTime;
                player.setCooldown(Material.TOTEM_OF_UNDYING, (int) (remainingTime / 50)); // Устанавливаем оставшееся время кулдауна
            } else {
                totemCooldowns.remove(playerId); // Если кулдаун уже истек, удаляем запись
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();

        if (totemCooldowns.containsKey(playerId)) {
            totemCooldowns.remove(playerId);
            player.setCooldown(Material.TOTEM_OF_UNDYING, 0);
        }
    }
}

