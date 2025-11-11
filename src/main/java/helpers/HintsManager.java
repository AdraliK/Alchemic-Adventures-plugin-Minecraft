package helpers;

import adralik.vanillaPlus.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import utils.Loadable;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HintsManager implements Loadable {
    private static final Set<UUID> disableHints = new HashSet<>();
    private static File file;
    private static FileConfiguration config;

    @Override
    public void register() {
        file = new File(Main.javaPlugin.getDataFolder(), "hints.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        if (config.isList("disabled")) {
            for (String uuid : config.getStringList("disabled")) {
                disableHints.add(UUID.fromString(uuid));
            }
        }
    }

    @Override
    public void shutdown() {
        save();
    }

    public static void save() {
        config.set("disabled", disableHints.stream().map(UUID::toString).toList());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasHints(UUID uuid) {
        return !disableHints.contains(uuid);
    }

    public static void enableHints(UUID uuid) {
        disableHints.remove(uuid);
        save();
    }

    public static void disableHints(UUID uuid) {
        disableHints.add(uuid);
        save();
    }
}