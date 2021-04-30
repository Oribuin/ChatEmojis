package xyz.oribuin.chatemojis.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.orilibrary.manager.Manager;
import xyz.oribuin.orilibrary.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmojiManager extends Manager {

    private final List<Emoji> cachedEmojis = new ArrayList<>();

    private FileConfiguration config;
    private ConfigurationSection section;

    public EmojiManager(ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        this.config = YamlConfiguration.loadConfiguration(FileUtils.createFile(this.getPlugin(), "emojis.yml"));
        this.section = this.config.getConfigurationSection("emojis");

        if (section == null) {
            this.getPlugin().getLogger().severe("We were unable to find the emojis configuration section, Please check your emojis.yml.");
            return;
        }

        this.cachedEmojis.clear();

        for (String key : section.getKeys(false)) {
            final Emoji emoji = new Emoji(key, section.getString(key + ".gui-name"), section.getString(key + ".check"), section.getString(key + ".replacement"));
            emoji.setCreator(UUID.fromString(section.getString(key + ".creator")));

            this.cachedEmojis.add(emoji);
        }

    }

    @Override
    public void disable() {
    }

    /**
     * Create and save an emoji into the configuration file.
     *
     * @param emoji The emoji
     */
    public void createEmoji(final Emoji emoji) {
        final String id = emoji.getId().toLowerCase();

        this.section.set(id + ".gui-name", emoji.getName());
        this.section.set(id + ".check", emoji.getCheck());
        this.section.set(id + ".replacement", emoji.getReplacement());
        this.section.set(id + ".creator", emoji.getCreator().toString());
        this.saveData();

        this.cachedEmojis.add(emoji);
    }

    /**
     * Delete an emoji from the configuration file.
     *
     * @param id The id of the emoji.
     */
    public void deleteEmoji(final String id) {
        this.section.set(id.toLowerCase(), null);
        this.saveData();

        this.cachedEmojis.removeIf(emoji -> emoji.getId().equalsIgnoreCase(id));
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    /**
     * Save the configuration file.
     */
    private void saveData() {
        try {
            this.config.save(this.getDataFile());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private File getDataFile() {
        return new File(this.getPlugin().getDataFolder(), "emojis.yml");
    }

    public List<Emoji> getCachedEmojis() {
        return cachedEmojis;
    }
}
