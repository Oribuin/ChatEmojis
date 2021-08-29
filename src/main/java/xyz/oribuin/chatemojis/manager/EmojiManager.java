package xyz.oribuin.chatemojis.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
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

            this.registerPermission(emoji);
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
        this.registerPermission(emoji);
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
        this.unregisterPermission("chatemojis.emoji." + id.toLowerCase());

        this.cachedEmojis.removeIf(emoji -> emoji.getId().equalsIgnoreCase(id));
    }

    /**
     * Check if the player can use a specific emoji.
     *
     * @param player The player being checked
     * @param emoji  The emoji with the permission.
     * @return true if they can.
     */
    public boolean canUseEmoji(Player player, Emoji emoji) {
        return player.hasPermission("chatemojis.emoji." + emoji.getId().toLowerCase());
    }

    /**
     * Register an emoji's permission into bukkit
     *
     * @param emoji The emoji with the permission.
     */
    public void registerPermission(Emoji emoji) {
        Permission parentPerm = Bukkit.getPluginManager().getPermission("chatemojis.emoji.*");
        if (parentPerm == null) {
            parentPerm = new Permission("chatemojis.emoji.*", "Access to all emojis.", PermissionDefault.OP);
            Bukkit.getPluginManager().addPermission(parentPerm);
        }

        Permission permission = Bukkit.getPluginManager().getPermission("chatemojis.emoji." + emoji.getId().toLowerCase());
        if (permission == null) {
            permission = new Permission("chatemojis.emoji." + emoji.getId().toLowerCase(), "Access to use the " + emoji.getName() + " emoji", PermissionDefault.OP);
            permission.addParent(parentPerm, true);
            Bukkit.getPluginManager().addPermission(permission);
        }
    }

    /**
     * Unregister a plugin permission if it exists.
     *
     * @param permission The permission being unregistered.
     */
    public void unregisterPermission(String permission) {
        final Permission perm = Bukkit.getPluginManager().getPermission(permission);
        if (perm != null)
            Bukkit.getPluginManager().removePermission(perm);
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
