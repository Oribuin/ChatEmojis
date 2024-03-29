package xyz.oribuin.chatemojis.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PAPI {
    private static Boolean enabled;

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
