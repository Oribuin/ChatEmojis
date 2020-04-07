package xyz.oribuin.chatemojis.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.menus.MenuEmojiList;
import xyz.oribuin.chatemojis.utils.Color;

import java.io.File;

public class CmdEmojis implements CommandExecutor {

    private ChatEmojis chatEmojis = ChatEmojis.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = chatEmojis.getConfig();
        FileConfiguration msgConfig = YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "messages.yml"));
        FileConfiguration emojiConfig = YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "emojis.yml"));

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("chatemojis.reload")) {
            chatEmojis.reloadConfig();
            YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "messages.yml"));
            YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "emojis.yml"));

            sender.sendMessage(Color.msg(msgConfig.getString("reload").replace("%version%", chatEmojis.getDescription().getVersion())));
            // Notify Console that the plugin was reloaded.
            Bukkit.getConsoleSender().sendMessage(Color.msg("&bReloaded " + chatEmojis.getDescription().getName() + " &f(&b" + chatEmojis.getDescription().getVersion() + "&f)"));
            return true;
        }



        if (args.length >= 1 && Bukkit.getPlayer(args[0]) != null) {
            new MenuEmojiList(Bukkit.getPlayer(args[0])).openFor();
            return true;
        }

        if (args.length >= 1 && Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(Color.msg(msgConfig.getString("invalid-player")));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You cannot execute this command as you are not a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("chatemojis.menu")) {
            player.sendMessage(Color.msg(msgConfig.getString("invalid-permission")));
            return true;
        }

        if (config.getStringList("disabled-worlds").contains(player.getWorld().getName())) {
            player.sendMessage(Color.msg(msgConfig.getString("disabled-world")));
            return true;
        }

        new MenuEmojiList(player).openFor();

        return true;
    }
}
