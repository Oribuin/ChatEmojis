package xyz.oribuin.chatemojis.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.utils.Chat;

import java.io.File;

public class CmdReload implements CommandExecutor {

    ChatEmojis plugin;

    public CmdReload(ChatEmojis instance) {
        this.plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = plugin.getConfig();

        FileConfiguration emojiConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "emojis.yml"));

        if (sender instanceof Player) {
            Player player = (Player) sender;


            /*
             * If the user does not have permission to use the reload command.
             * Print "no-permission" message to the player.
             */

            if (!player.hasPermission("chatemojis.reload")) {
                player.sendMessage(Chat.cl(config.getString("no-permission")));
                return true;
            }
        }

        // Reload the configuration
        plugin.reloadConfig();
        YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "emojis.yml"));


        sender.sendMessage(Chat.cl(config.getString("reload").replaceAll("\\{version}", plugin.getDescription().getVersion())));
        // Notify Console that the plugin was reloaded.
        Bukkit.getConsoleSender().sendMessage(Chat.cl("&bReloaded " + plugin.getDescription().getName() + " &f(&b" + plugin.getDescription().getVersion() + "&f)"));
        return true;
    }
}
