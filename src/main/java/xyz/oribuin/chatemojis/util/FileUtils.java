package xyz.oribuin.chatemojis.util;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtils {

    /**
     * Creates a file on disk from a file located in the jar
     *
     * @param fileName The name of the file to create
     */
    public static void createFile(Plugin plugin, String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            try (InputStream inStream = plugin.getResource(fileName)) {
                if (inStream == null) {
                    file.createNewFile();
                    return;
                }

                Files.copy(inStream, Paths.get(file.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create a the menu file in a plugin in the menu folder.
     *
     * @param plugin   The plugin the file is being created in
     * @param fileName The name of the menu file because created
     */
    public static void createMenuFile(Plugin plugin, String fileName) {
        File dir = new File(plugin.getDataFolder(), "menus");
        File file = new File(dir, fileName + ".yml");

        if (!dir.exists())
            dir.mkdir();

        if (!file.exists()) {
            try (InputStream stream = plugin.getResource("menus/" + fileName + ".yml")) {

                if (stream == null) {
                    file.createNewFile();
                    return;
                }

                Files.copy(stream, Paths.get(file.getAbsolutePath()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}