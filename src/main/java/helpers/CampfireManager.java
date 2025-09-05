package helpers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static adralik.vanillaPlus.Main.javaPlugin;

public class CampfireManager {

    private static FileConfiguration config;
    private static File file;
    private static final Set<String> campfires = new HashSet<>();

    public static void init() {
        file = new File(javaPlugin.getDataFolder(), "campfires.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        campfires.addAll(config.getStringList("campfires"));
    }

    public static void save() {
        config.set("campfires", new ArrayList<>(campfires));
        try { config.save(file); } catch (IOException e) { e.printStackTrace(); }
    }

    public static void addCampfire(Location loc) {
        campfires.add(toString(loc));
        save();
    }

    public static void removeCampfire(Location loc) {
        campfires.remove(toString(loc));
        save();
    }

    public static List<Location> getCampfires() {
        List<Location> list = new ArrayList<>();
        for (String s : campfires) {
            String[] parts = s.split(":");
            World world = Bukkit.getWorld(parts[0]);
            if (world != null) {
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                list.add(new Location(world, x, y, z));
            }
        }
        return list;
    }

    private static String toString(Location loc) {
        return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ();
    }
}

