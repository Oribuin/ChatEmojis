package xyz.oribuin.chatemojis.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PluginPlaceholders extends PlaceholderExpansion {
    private ChatEmojis plugin = ChatEmojis.getInstance();

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        FileConfiguration emojiConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "emojis.yml"));
        ConfigurationSection emojiSec = emojiConfig.getConfigurationSection("emojis");
        if (emojiSec == null) return null;

        if (placeholder == null)
            return null;

        if (placeholder.equalsIgnoreCase("total"))
            return String.valueOf(emojiSec.getKeys(false).stream().filter(emoji -> player.hasPermission(emoji + ".permission")).count());

        return null;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "ChatEmojis".toLowerCase();
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
