package xyz.oribuin.chatemojis.utils;

import org.bukkit.ChatColor;

public class Color {
    public static String msg(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
