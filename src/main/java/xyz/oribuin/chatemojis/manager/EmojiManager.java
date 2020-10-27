package xyz.oribuin.chatemojis.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class EmojiManager extends Manager {

    private final static String EMOJI_CONFIG = "emojis.yml";

    private FileConfiguration emojiConfig;

    public EmojiManager(ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void reload() {
        FileUtils.createFile(plugin, EMOJI_CONFIG);
        this.emojiConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), EMOJI_CONFIG));
    }

    /**
     * Create the emoji and add it to emojis.yml
     */
    public void createEmoji(Player creator, String name, String check, String replacement) {
        emojiConfig.set("emojis." + name.toLowerCase() + ".gui-name", name);
        emojiConfig.set("emojis." + name.toLowerCase() + ".check", check.toLowerCase());
        emojiConfig.set("emojis." + name.toLowerCase() + ".replacement", replacement);
        if (creator != null)
            emojiConfig.set("emojis." + name.toLowerCase() + ".creator", creator.getUniqueId().toString());

        this.saveData();
    }

    public void removeEmoji(String name) {
        emojiConfig.set("emojis." + name.toLowerCase(), null);
        this.saveData();
    }

    public ConfigurationSection getEmojiSec() {
        return this.emojiConfig.getConfigurationSection("emojis");
    }

    public FileConfiguration getEmojiConfig() {
        return this.emojiConfig;
    }

    public int getEmojiCreated(Player player) {
        return (int) getEmojiSec().getKeys(false).stream().filter(emoji -> player.getUniqueId().equals(UUID.fromString(Objects.requireNonNull(getEmojiSec().getString(emoji + ".creator"))))).count();
    }

    public int getEmojiTotal() {
        return getEmojiSec().getKeys(false).size();
    }

    /**
     * Save the Emojis.yml File
     */

    private void saveData() {
        try {
            this.emojiConfig.save(this.getDataFile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private File getDataFile() {
        return new File(this.plugin.getDataFolder(), EMOJI_CONFIG);
    }
}
