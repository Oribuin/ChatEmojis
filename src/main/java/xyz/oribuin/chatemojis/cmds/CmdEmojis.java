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
import java.io.IOException;

public class CmdEmojis implements CommandExecutor {

    private ChatEmojis chatEmojis = ChatEmojis.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = chatEmojis.getConfig();
        FileConfiguration msgConfig = YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "messages.yml"));


        if (args.length >= 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("chatemojis.reload")) {
            chatEmojis.reloadConfig();
            YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "messages.yml"));
            YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "emojis.yml"));

            sender.sendMessage(Color.msg(msgConfig.getString("reload").replace("%version%", chatEmojis.getDescription().getVersion())));
            // Notify Console that the plugin was reloaded.
            Bukkit.getConsoleSender().sendMessage(Color.msg("&bReloaded " + chatEmojis.getDescription().getName() + " &f(&b" + chatEmojis.getDescription().getVersion() + "&f)"));
            return true;
        }

        if (args.length >= 4 && args[0].equalsIgnoreCase("create") && sender.hasPermission("chatemojis.create")) {
            FileConfiguration emojiConfig = YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "emojis.yml"));

            final String msg = String.join(" ", args);
            final String replacement = msg.substring(args[0].length() + args[1].length() + args[2].length() + 3);
            emojiConfig.set("emojis." + args[1].toLowerCase() + ".name", "" + args[1]);
            emojiConfig.set("emojis." + args[1].toLowerCase() + ".check", args[2].toLowerCase());
            emojiConfig.set("emojis." + args[1].toLowerCase() + ".replacement", replacement);
            emojiConfig.set("emojis." + args[1].toLowerCase() + ".permission", "emoji." + args[1].toLowerCase());

            try {
                emojiConfig.save(new File(chatEmojis.getDataFolder(), "emojis.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            sender.sendMessage(Color.msg(msgConfig.getString("emoji-create")
                    .replaceAll("%emoji%", args[1].toLowerCase())
                    .replaceAll("%check%", args[2].toLowerCase())
                    .replaceAll("%replacement%", replacement)
                    .replaceAll("%permission%", "emoji." + args[1].toLowerCase())));
            return true;
        }

        if (args.length >= 1 && Bukkit.getPlayer(args[0]) != null) {
            if (!sender.hasPermission("chatemojis.menu.other")) {
                sender.sendMessage(Color.msg(msgConfig.getString("invalid-permission")));
                return true;
            }

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
