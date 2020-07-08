package xyz.oribuin.chatemojis.managers;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.hooks.PlaceholderAPIHook;
import xyz.oribuin.chatemojis.utils.FileUtils;
import xyz.oribuin.chatemojis.utils.HexUtils;
import xyz.oribuin.chatemojis.utils.StringPlaceholders;

import java.io.File;

public class MessageManager extends Manager {
    private final static String MESSAGE_CONFIG = "messages.yml";

    private static FileConfiguration messageConfig;

    public MessageManager(ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void reload() {
        FileUtils.createFile(this.plugin, MESSAGE_CONFIG);
        messageConfig = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), MESSAGE_CONFIG));

    }

    public void sendMessage(CommandSender sender, String messageId) {
        this.sendMessage(sender, messageId, StringPlaceholders.empty());
    }

    public void sendMessage(CommandSender sender, String messageId, StringPlaceholders placeholders) {

        if (messageConfig.getString(messageId) == null) {
            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify("{#ff4072}" + messageId + " is null in messages.yml")));
            return;
        }

        if (!messageConfig.getString(messageId).isEmpty()) {
            final String msg = messageConfig.getString("prefix") + placeholders.apply(messageConfig.getString(messageId));

            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify(this.parsePlaceholders(sender, msg))));
        }
    }

    private String parsePlaceholders(CommandSender sender, String message) {
        if (sender instanceof Player)
            return PlaceholderAPIHook.apply((Player) sender, message);
        return message;
    }
}
