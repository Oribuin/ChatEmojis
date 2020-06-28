package xyz.oribuin.chatemojis.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtils {
    private static final Pattern regex = Pattern.compile("\\{#([A-Fa-f0-9]){6}}");

    public static BaseComponent[] parseHexColors(String message) {
        String parsed = message;

        if (NMSUtils.getVersionNumber() >= 16) {
            Matcher matcher = regex.matcher(parsed);

            while (matcher.find()) {
                String hexString = matcher.group();
                hexString = hexString.substring(1, hexString.length() - 1);

                final ChatColor hexColor = ChatColor.of(hexString);
                final String before = parsed.substring(0, matcher.start());
                final String after = parsed.substring(matcher.end());

                parsed = before + hexColor + after;
                matcher = regex.matcher(parsed);
            }
        }

        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', parsed));
    }

    private static class NMSUtils {

        private static String cachedVersion = null;
        private static int cachedVersionNumber = -1;

        /**
         * Gets the server version
         *
         * @return The server version
         */
        public static String getVersion() {
            if (cachedVersion == null) {
                String name = Bukkit.getServer().getClass().getPackage().getName();
                cachedVersion = name.substring(name.lastIndexOf('.') + 1) + ".";
            }
            return cachedVersion;
        }

        /**
         * Gets the server version major release number
         *
         * @return The server version major release number
         */
        public static int getVersionNumber() {
            if (cachedVersionNumber == -1) {
                String name = getVersion().substring(3);
                cachedVersionNumber = Integer.parseInt(name.substring(0, name.length() - 4));
            }
            return cachedVersionNumber;
        }
    }
}