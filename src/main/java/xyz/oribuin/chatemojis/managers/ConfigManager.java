package xyz.oribuin.chatemojis.managers;

import org.bukkit.configuration.file.FileConfiguration;
import xyz.oribuin.chatemojis.ChatEmojis;

import java.util.Arrays;
import java.util.List;

public class ConfigManager extends Manager {

    public ConfigManager(ChatEmojis plugin) {
        super(plugin);
        this.reload();
    }

    @Override
    public void reload() {
        // Reload config
        this.plugin.reloadConfig();

        // Save default configuration
        this.plugin.saveDefaultConfig();

        // Define the configuration
        FileConfiguration config = this.plugin.getConfig();

        for (Setting setting : Setting.values()) {
            // Set the value if it does not exist
            setting.setIfNotExists(config);

            // Load value
            setting.load(config);
        }
    }

    public enum Setting {

        CMD_SETTINGS_ECO_ENABLED("command-settings.economy", true),
        CMD_SETTINGS_ECO_CREATE("command-settings.create-price", 500),
        CMD_SETTINGS_PERM_ENABLED("command-settings.add-perm", false),
        DISABLED_WORLDS("disabled-worlds", Arrays.asList("disabled-world-1", "disabled-world-2")),
        GUI_NAME("menu.title", "Emoji Count | emojis"),
        GUI_BORDER("menu.border", "GRAY_STAINED_GLASS_PANE"),
        GUI_USE_SOUND("menu.use-sound", true),
        GUI_CLICK_SOUND("menu.click-sound", "ENTITY_ARROW_HIT_PLAYER");


        private final String key;
        private final Object defaultValue;
        private Object value = null;

        Setting(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        public void setIfNotExists(FileConfiguration config) {
            this.loadValue();
            this.value = ChatEmojis.getInstance().getConfig().get(this.key);

            if (this.value == null) {
                config.set(this.key, this.defaultValue);
            }

        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();

            return (boolean) this.value;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();

            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();

            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();

            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();

            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();

            return (String) this.value;
        }

        private double getNumber() {
            this.loadValue();

            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();

            return (List<String>) this.value;
        }

        /**
         * Loads the value from the config and caches it
         */
        private void load(FileConfiguration config) {
            this.value = config.get(this.key);
        }

        private void loadValue() {
            if (this.value != null)
                return;

            this.value = ChatEmojis.getInstance().getConfig().get(this.key);

            if (this.value == null) {
                ChatEmojis.getInstance().getConfig().set(this.key, this.defaultValue);
            }
        }
    }
}
