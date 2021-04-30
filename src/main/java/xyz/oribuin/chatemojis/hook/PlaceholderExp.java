package xyz.oribuin.chatemojis.hook;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;

public class PlaceholderExp extends PlaceholderExpansion {
    private final ChatEmojis plugin;

    public PlaceholderExp(ChatEmojis plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String placeholder) {
        final EmojiManager manager = this.plugin.getManager(EmojiManager.class);

        if (placeholder.equalsIgnoreCase("unlocked")) {
            if (player.hasPermission("chatemojis.emoji.*")) return String.valueOf(manager.getCachedEmojis().size());

            return String.valueOf(manager.getCachedEmojis().stream().filter(emoji -> player.hasPermission("chatemojis.emoji." + emoji.getId().toLowerCase())).count());
        }

        if (placeholder.equalsIgnoreCase("total")) {
            return String.valueOf(manager.getCachedEmojis().size());
        }

        if (placeholder.equalsIgnoreCase("created")) {
            return String.valueOf(manager.getCachedEmojis().stream().filter(emoji -> emoji.getCreator() != null && emoji.getCreator().equals(player.getUniqueId())).count());
        }


        return null;
    }
    @Override
    public @NotNull String getIdentifier() {
        return "ChatEmojis".toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
