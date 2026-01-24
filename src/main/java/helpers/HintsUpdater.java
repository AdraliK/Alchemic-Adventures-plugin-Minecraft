package helpers;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import utils.ActionBarLock;
import utils.Loadable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HintsUpdater extends BukkitRunnable implements Loadable {

    private static final long UPDATE_PERIOD = 10L;
    private static final List<Consumer<Player>> tasks = new ArrayList<>();

    @Override
    public void register() {
        this.runTaskTimer(Main.javaPlugin, 0L, UPDATE_PERIOD);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!HintsManager.hasHints(player.getUniqueId())) continue;
            if (ActionBarLock.isLocked(player)) continue;
            for (Consumer<Player> task : tasks) {
                task.accept(player);
            }
        }
    }

    public static void addTask(Consumer<Player> task) {
        tasks.add(task);
    }
}
