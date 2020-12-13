package xyz.oribuin.chatemojis.command.subcommand;

import org.bukkit.command.CommandSender;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.orilibrary.StringPlaceholders;
import xyz.oribuin.orilibrary.SubCommand;

public class CmdReload extends SubCommand {
    private final ChatEmojis plugin;

    public CmdReload(ChatEmojis plugin, CmdEmoji command) {
        super(command, "reload");

        this.plugin = plugin;
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {
        MessageManager msgM = this.plugin.getManager(MessageManager.class);
        if (!sender.hasPermission("chatemojis.reload")) {
            msgM.sendMessage(sender, "invalid-permission");
            return;
        }

        this.plugin.reload();
        msgM.sendMessage(sender, "reload", StringPlaceholders.single("version", this.plugin.getDescription().getVersion()));

    }

}