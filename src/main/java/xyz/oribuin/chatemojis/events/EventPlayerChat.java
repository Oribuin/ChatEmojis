package xyz.oribuin.chatemojis.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.utils.Color;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventPlayerChat implements Listener {

    private ChatEmojis chatEmojis = ChatEmojis.getInstance();

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        FileConfiguration emojiConfig = YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "emojis.yml"));
        ConfigurationSection emojiSec = emojiConfig.getConfigurationSection("emojis");
        if (emojiSec == null) return;

        Player player = event.getPlayer();

        if (chatEmojis.getConfig().getString("disabled-worlds").contains(player.getWorld().getName())) return;

        for (String list : emojiSec.getKeys(false)) {

            String permission = emojiSec.getString(list + ".permission");
            String check = emojiSec.getString(list + ".check");
            String replacement = emojiSec.getString(list + ".replacement");

            if (permission == null) continue;
            if (!player.hasPermission(permission)) continue;
            if (check == null) continue;
            if (replacement == null) continue;

            if (event.getMessage().toLowerCase().contains(check.toLowerCase())) {
                event.setMessage(Color.msg(event.getMessage().replaceAll(Pattern.quote(check), Matcher.quoteReplacement(replacement))));
            }
        }
    }
}
