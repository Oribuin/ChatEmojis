package xyz.oribuin.chatemojis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.utils.Chat;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiSend implements Listener {

    private ChatEmojis plugin;

    public EmojiSend(ChatEmojis plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true)
    public void onEmojiSend(AsyncPlayerChatEvent event) {
        /*
         * Get the emojis.yml configuration file
         */
        FileConfiguration emojiConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "emojis.yml"));

        // Check if emojis in emojis.yml == null.
        if (emojiConfig.getConfigurationSection("emojis") == null) return;

        /*
         * Define the player for whatever reason?
         */

        Player player = event.getPlayer();

        for (String list : emojiConfig.getConfigurationSection("emojis").getKeys(false)) {

            /*
             * Define the permission string
             * Define the check string
             * Define the replacement string
             */

            String permission = emojiConfig.getString("emojis." + list + ".permission");
            String check = emojiConfig.getString("emojis." + list + ".check");
            String replacement = emojiConfig.getString("emojis." + list + ".replacement");

            // If the permission is null/doesn't exist, do nothing
            if (permission == null) continue;

            // If the player does not have permission to use the emoji, don't do anything.
            if (!player.hasPermission(permission)) continue;

            // If the check is null, do nothing.
            if (check == null) continue;
            /**
             * If the config contains check and replacement is not null
             * Get the message and change check to replacement
             */
            if (event.getMessage().toLowerCase().contains(check.toLowerCase())) {
                if (replacement != null)
                    event.setMessage(Chat.cl(event.getMessage().replaceAll(Pattern.quote(check), Matcher.quoteReplacement(replacement))));
            }
        }
    }
}
