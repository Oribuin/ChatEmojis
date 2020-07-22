package xyz.oribuin.chatemojis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.oribuin.chatemojis.ChatEmojis;

public class PlayerChat implements Listener {

    private final ChatEmojis plugin;

    public PlayerChat(ChatEmojis plugin) {
        this.plugin = plugin;

    }

    @EventHandler(priority = EventPriority.LOWEST)
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

    /*
    @EventHandler
    public void onCommandProcess(PlayerCommandPreprocessEvent event) {
        ConfigurationSection section = this.plugin.getEmojiManager().getEmojiSec();
        Player player = event.getPlayer();
        /*
        if (!plugin.getConfig().getBoolean("hooks.plotsquared.enabled") || Bukkit.getPluginManager().getPlugin("PlotSquared") == null)
            return;

        if (plugin.getConfig().getStringList("disabled-worlds").contains(player.getWorld().getName())) return;

        for (String emoji : section.getKeys(false)) {
            String check = section.getString(emoji + ".check");
            String replacement = section.getString(emoji + ".replacement");

            if (!player.hasPermission("chatemojis.emojis.*") && !player.hasPermission("chatemojis.emoji." + emoji)) continue;
            if (check == null) continue;
            if (replacement == null) continue;

            event.setMessage(event.getMessage().replace(check.toLowerCase(), replacement.toLowerCase()));
        }
    }
    */
}
