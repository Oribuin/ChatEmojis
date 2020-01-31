package xyz.oribuin.chatemojis.utils;

import org.bukkit.ChatColor;

public class Chat {
    public static String cl(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
