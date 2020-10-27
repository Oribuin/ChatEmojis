package xyz.oribuin.chatemojis.menu;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.util.FileUtils;

import java.io.File;

public abstract class Menu {
    private final ChatEmojis plugin;
    private final String guiName;

    private final FileConfiguration menuConfig;

    public Menu(ChatEmojis plugin, String guiName) {
        this.plugin = plugin;
        this.guiName = guiName;

        FileUtils.createMenuFile(plugin, guiName);
        this.menuConfig = YamlConfiguration.loadConfiguration(this.getMenuFile());
    }

    public void reload() {
        FileUtils.createMenuFile(plugin, guiName);
        YamlConfiguration.loadConfiguration(this.getMenuFile());
    }

    public FileConfiguration getMenuConfig() {
        return menuConfig;
    }

    public String getGuiName() {
        return guiName.toLowerCase();
    }

    private File getMenuFile() {
        return new File(this.plugin.getDataFolder() + File.separator + "menus", this.getGuiName() + ".yml");
    }
}
