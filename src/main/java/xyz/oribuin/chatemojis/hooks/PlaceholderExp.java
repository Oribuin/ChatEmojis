package xyz.oribuin.chatemojis.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;

import java.util.Objects;
import java.util.UUID;

public class PlaceholderExp extends PlaceholderExpansion {
    private final ChatEmojis plugin;

    public PlaceholderExp(ChatEmojis plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        ConfigurationSection emojiSec = this.plugin.getEmojiManager().getEmojiSec();
        if (emojiSec == null) return null;

        if (placeholder == null)
            return null;

        if (placeholder.equalsIgnoreCase("unlocked"))
            return String.valueOf(emojiSec.getKeys(false).stream().filter(emoji -> player.hasPermission("chatemojis.emoji." + emoji)).count());

        if (placeholder.equalsIgnoreCase("total"))
            return String.valueOf(plugin.getEmojiManager().getEmojiTotal());

        if (placeholder.equals("created"))
            return String.valueOf(plugin.getEmojiManager().getEmojiCreated(player));

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
