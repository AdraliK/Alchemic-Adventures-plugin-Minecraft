package helpers;

import adralik.vanillaPlus.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Updater extends BukkitRunnable {

    private static final long UPDATE_PERIOD = 10L;
    private static final List<Consumer<Player>> tasks = new ArrayList<>();

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Consumer<Player> task : tasks) {
                task.accept(player);
            }
        }
    }

    public void start() {
        this.runTaskTimer(Main.javaPlugin, 0L, UPDATE_PERIOD);
    }

    public static void addTask(Consumer<Player> task) {
        tasks.add(task);
    }

    public static void removeTask(Consumer<Player> task) {
        tasks.remove(task);
    }
}
