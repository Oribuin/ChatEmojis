package xyz.oribuin.chatemojis.listener;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.oribuin.chatemojis.ChatEmojis;

public class PlayerChat implements Listener {

    private final ChatEmojis plugin;

    public PlayerChat(ChatEmojis plugin) {
        this.plugin = plugin;

    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        ConfigurationSection emojiSec = this.plugin.getEmojiManager().getEmojiSec();
        Player player = event.getPlayer();

        if (plugin.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) return;

        for (String emoji : emojiSec.getKeys(false)) {
            String check = emojiSec.getString(emoji + ".check");
            String replacement = emojiSec.getString(emoji + ".replacement");

            if (!player.hasPermission("chatemojis.emoji.*") && !player.hasPermission("chatemojis.emoji." + emoji)) continue;
            if (check == null) continue;
            if (replacement == null) continue;

            event.setMessage(event.getMessage().replace(check.toLowerCase(), replacement));
        }
    }
}
