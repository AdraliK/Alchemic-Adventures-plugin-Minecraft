package listeners.world;

import adralik.vanillaPlus.Main;
import helpers.DatapackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static adralik.vanillaPlus.Main.config;

public class MoonPhaseChecker implements Listener {

    private final String configPath = "events.full-moon.messages";

    private final String warningMessageTitle = config.getString(configPath + ".warning-message.title");
    private final String warningMessageSubtitle = config.getString(configPath + ".warning-message.subtitle");

    private final String peaceMessageTitle = config.getString(configPath + ".peace-message.title");
    private final String peaceMessageSubtitle = config.getString(configPath + ".peace-message.subtitle");

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
                    playSoundAndMessageToAllPlayers(world);
                    isStart = true;
                } else if (!isFullMoon(world) && isStart) {
                    sendPeaceMessageForAllPlayers(world);
                    isStart = false;
                }
            }
        }.runTaskTimer(Main.javaPlugin, 0L, 60L);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();

        if (world.getEnvironment() != World.Environment.NORMAL) return;
        if (!isFullMoon(world)) return;

        playSoundAndSendWarningMessageTo(player);
    }

    public static boolean isFullMoon(World world) {
        long time = world.getFullTime();
        long days = time / 24000;
        int phase = (int) (days % 8); // 8 фаз луны, полнолуние - это 0

        // Проверка фазы луны (полнолуние) и того, что время между 13000 и 23000 (ночь)
        long currentTimeOfDay = time % 24000; // Текущее время в пределах одного дня
        boolean isNight = currentTimeOfDay >= 13000 && currentTimeOfDay <= 23000;

        return phase == 0 && isNight;
    }

    private void playSoundAndMessageToAllPlayers(World world) {
        for (Player player : world.getPlayers()) {
            playSoundAndSendWarningMessageTo(player);
        }
    }

    private void playSoundAndSendWarningMessageTo(Player player) {
        player.sendTitle(warningMessageTitle, warningMessageSubtitle, 10, 70, 20);
        player.playSound(player, Sound.ENTITY_RAVAGER_STUNNED, 0.5f, 0.2f);
        DatapackUtils.grantAdvancement(player, "moon");
    }

    private void sendPeaceMessageForAllPlayers(World world) {
        for (Player player : world.getPlayers()) {
            player.sendTitle(peaceMessageTitle, peaceMessageSubtitle, 10, 70, 20);
        }
    }
}

