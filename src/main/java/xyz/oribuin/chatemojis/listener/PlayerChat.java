package xyz.oribuin.chatemojis.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.obj.Emoji;

public class PlayerChat implements Listener {

    private final ChatEmojis plugin;

    public PlayerChat(ChatEmojis plugin) {
        this.plugin = plugin;

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) return;

        for (Emoji emoji : this.plugin.getManager(EmojiManager.class).getCachedEmojis()) {

            if (!player.hasPermission("chatemojis.emoji.*") && !player.hasPermission("chatemojis.emoji." + emoji.getId())) continue;

            event.setMessage(event.getMessage().replace(emoji.getCheck().toLowerCase(), emoji.getReplacement()));
        }
    }
}
