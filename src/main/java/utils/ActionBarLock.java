package utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionBarLock {

    private static final Map<UUID, Long> locks = new HashMap<>();

    public static void lock(Player player, long millis) {
        locks.put(player.getUniqueId(), System.currentTimeMillis() + millis);
    }

    public static boolean isLocked(Player player) {
        return locks.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis();
    }

}
