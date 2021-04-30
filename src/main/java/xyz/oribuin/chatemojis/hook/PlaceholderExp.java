package xyz.oribuin.chatemojis.hook;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;

public class PlaceholderExp extends PlaceholderExpansion {
    private final ChatEmojis plugin;

    public PlaceholderExp(ChatEmojis plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
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
