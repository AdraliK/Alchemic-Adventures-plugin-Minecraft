package listeners.world;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class MoonPhaseChecker implements Listener {

    private boolean isStart = false;

    public MoonPhaseChecker() {
        startMoonPhaseChecker();
    }

    private void startMoonPhaseChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorlds().stream()
                        .filter(w -> w.getEnvironment() == World.Environment.NORMAL)
                        .findFirst().orElse(null);
                if (world != null && isFullMoon(world) && !isStart) {
                    playSoundAndMessageForAllPlayers(world);
                    isStart = true;
                } else if (!isFullMoon(world) && isStart) {
                    sendMessageForAllPlayers(world);
                    isStart = false;
                }
            }
        }.runTaskTimer(Main.javaPlugin, 0L, 60L);
    }

    private boolean isFullMoon(World world) {
        long time = world.getFullTime();
        long days = time / 24000;
        int phase = (int) (days % 8); // 8 фаз луны, полнолуние - это 0

        // Проверка фазы луны (полнолуние) и того, что время между 13000 и 23000 (ночь)
        long currentTimeOfDay = time % 24000; // Текущее время в пределах одного дня
        boolean isNight = currentTimeOfDay >= 13000 && currentTimeOfDay <= 23000;

        return phase == 0 && isNight;
    }

    private void playSoundAndMessageForAllPlayers(World world) {
        for (Player player : world.getPlayers()) {
            player.sendTitle("§cПолная Луна!", "§cБудьте осторожны!", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 0.35f, 1.0f);
        }
    }

    private void sendMessageForAllPlayers(World world) {
        for (Player player : world.getPlayers()) {
            player.sendTitle("§dТьма уходит", "§dПолнолуние подошло к концу!", 10, 70, 20);
        }
    }
}

