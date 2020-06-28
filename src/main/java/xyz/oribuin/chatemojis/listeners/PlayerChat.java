package xyz.oribuin.chatemojis.listeners;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.oribuin.chatemojis.ChatEmojis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerChat implements Listener {

    private final ChatEmojis plugin;

    public PlayerChat(ChatEmojis plugin) {
        this.plugin = plugin;

    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        ConfigurationSection emojiSec = this.plugin.getEmojiManager().getEmojiConfig();
        if (emojiSec == null)
            return;

        Player player = event.getPlayer();

        if (plugin.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) return;

        for (String list : emojiSec.getKeys(false)) {

            String name = emojiSec.getString(list + ".name");
            String check = emojiSec.getString(list + ".check");
            String replacement = emojiSec.getString(list + ".replacement");

            if (name == null) continue;
            if (!player.hasPermission("chatemojis.emoji." + list)) continue;
            if (check == null) continue;
            if (replacement == null) continue;

            if (event.getMessage().toLowerCase().contains(check.toLowerCase())) {
                event.setMessage(event.getMessage().replace(Pattern.quote(check), this.plugin.getMessageManager().parseColors(Matcher.quoteReplacement(replacement + ChatColor.RESET))));
            }
        }
    }
}
