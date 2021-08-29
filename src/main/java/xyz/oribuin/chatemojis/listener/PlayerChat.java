package xyz.oribuin.chatemojis.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;

public class PlayerChat implements Listener {

    private final ChatEmojis plugin;
    private final EmojiManager emojiManager;

    public PlayerChat(ChatEmojis plugin) {
        this.plugin = plugin;
        this.emojiManager = this.plugin.getManager(EmojiManager.class);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName()))
            return;

        if (this.plugin.getToggleList().contains(player.getUniqueId()))
            return;

        emojiManager.getCachedEmojis()
                .stream()
                .filter(emoji -> emojiManager.canUseEmoji(player, emoji))
                .forEach(emoji -> event.setMessage(event.getMessage().replace(emoji.getCheck().toLowerCase(), emoji.getReplacement())));

    }
}
