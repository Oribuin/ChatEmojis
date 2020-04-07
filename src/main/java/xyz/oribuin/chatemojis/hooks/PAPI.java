package xyz.oribuin.chatemojis.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;

public class PAPI {
    ChatEmojis plugin;

    private static Boolean enabled;

    public PAPI(ChatEmojis plugin) {
        this.plugin = plugin;
    }

    public static boolean enabled() {
        if (enabled != null)
            return enabled;
        return enabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static String apply(Player player, String text) {
        if (enabled())
            return PlaceholderAPI.setPlaceholders(player, text);
        return text;
    }

}
