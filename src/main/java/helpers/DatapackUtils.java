package helpers;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DatapackUtils {

    private static final String DATAPACK_PATH = "world/datapacks/vanillaplus";
    private static final String DATAPACK_RESOURCE_PATH = "datapacks/vanillaplus/";
    private static JavaPlugin plugin;

    public static void setupDatapack(JavaPlugin plugin, File worldContainer) {
        DatapackUtils.plugin = plugin;

        File datapackFolder = new File(worldContainer, DATAPACK_PATH);

        if (datapackFolder.exists() && datapackFolder.isDirectory() && datapackFolder.list().length > 0) {
            return;
        }

        copyAllDatapackFiles(plugin, worldContainer);

        plugin.getLogger().info("Датапак установлен в: " + datapackFolder.getPath());
    }

    private static void copyAllDatapackFiles(JavaPlugin plugin, File worldContainer) {

        if (plugin.getResource(DATAPACK_RESOURCE_PATH) == null) {
            plugin.getLogger().warning("Папка ресурсов " + DATAPACK_RESOURCE_PATH + " не найдена.");
            return;
        }

        File pluginJar = getPluginJar(plugin);
        if (pluginJar == null) {
            plugin.getLogger().severe("Не удалось определить JAR-файл плагина!");
            return;
        }

        try (JarFile jarFile = new JarFile(pluginJar)) {
            for (JarEntry entry : Collections.list(jarFile.entries())) {
                if (entry.getName().startsWith(DATAPACK_RESOURCE_PATH) && !entry.isDirectory()) {
                    String relativePath = entry.getName().substring(DATAPACK_RESOURCE_PATH.length());
                    File targetFile = new File(worldContainer, DATAPACK_PATH + "/" + relativePath);
                    copyFromJar(plugin, entry.getName(), targetFile);
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при копировании датапака: " + e.getMessage());
        }
    }

    private static void copyFromJar(JavaPlugin plugin, String resourcePath, File targetFile) {
        try (InputStream in = plugin.getResource(resourcePath)) {
            if (in == null) {
                plugin.getLogger().warning("Не удалось найти ресурс: " + resourcePath);
                return;
            }

            targetFile.getParentFile().mkdirs();
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при копировании " + resourcePath + ": " + e.getMessage());
        }
    }

    private static File getPluginJar(JavaPlugin plugin) {
        try {
            return new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            plugin.getLogger().severe("Ошибка при получении JAR-файла: " + e.getMessage());
            return null;
        }
    }

    public static void grantAdvancement(Player player, String advancementKey) {
        NamespacedKey key = new NamespacedKey("vanillaplus", advancementKey);
        Advancement advancement = Bukkit.getAdvancement(key);

        if (advancement == null) {
            plugin.getLogger().severe(":Достижение не найдено: " + advancementKey);
            return;
        }

        AdvancementProgress progress = player.getAdvancementProgress(advancement);

        if (progress.isDone()) return;

        for (String criterion : progress.getRemainingCriteria()) {
            progress.awardCriteria(criterion);
        }
    }
}
