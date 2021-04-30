package xyz.oribuin.chatemojis.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.orilibrary.manager.Manager;
import xyz.oribuin.orilibrary.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConfigManager extends Manager {

    private final ChatEmojis plugin = (ChatEmojis) getPlugin();
    private FileConfiguration config;

    public ConfigManager(final ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        this.config = YamlConfiguration.loadConfiguration(FileUtils.createFile(this.plugin, "config.yml"));

        // Set all missing values
        Arrays.stream(Setting.values())
                .filter(setting -> this.config.get(setting.key) == null)
                .forEach(setting -> this.config.set(setting.key, setting.defaultValue));

        try {
            this.config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void disable() {

    }

    private enum Setting {
        ;

        private final String key;
        private final Object defaultValue;

        Setting(final String key, final Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

    }

    public FileConfiguration getConfig() {
        return config;
    }

}
