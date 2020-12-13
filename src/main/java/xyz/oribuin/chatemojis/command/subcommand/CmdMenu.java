package xyz.oribuin.chatemojis.command.subcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.menu.MainMenu;
import xyz.oribuin.orilibrary.StringPlaceholders;
import xyz.oribuin.orilibrary.SubCommand;

public class CmdMenu extends SubCommand {
    private final ChatEmojis plugin;

    public CmdMenu(ChatEmojis plugin, CmdEmoji command) {
        super(command, "menu");

        this.plugin = plugin;
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {
        MessageManager msgM = this.plugin.getManager(MessageManager.class);
        if (args.length == 2) {
            Player mentioned = Bukkit.getPlayer(args[1]);

            if (!(sender.hasPermission("chatemojis.menu.other"))) {
                msgM.sendMessage(sender, "invalid-permission");
                return;
            }

            // Check if mentioned is null, offline, vanished
            if (mentioned == null || !mentioned.isOnline() || mentioned.hasMetadata("vanished")) {
                // Send invalid player
                msgM.sendMessage(sender, "invalid-player");
                return;
            }


            new MainMenu(plugin, mentioned).openGui();
            msgM.sendMessage(sender, "opened-menu", StringPlaceholders.single("player", mentioned.getName()));
            return;
        }

        if (!(sender instanceof Player)) {
            msgM.sendMessage(sender, "player-only");
            return;
        }

        if (!(sender.hasPermission("chatemojis.menu"))) {
            msgM.sendMessage(sender, "invalid-permission");
            return;
        }

        Player player = (Player) sender;
        new MainMenu(plugin, player).openGui();
    }

}