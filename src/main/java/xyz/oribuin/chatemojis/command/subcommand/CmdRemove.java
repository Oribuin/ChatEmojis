package xyz.oribuin.chatemojis.command.subcommand;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.command.SubCommand;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.util.StringPlaceholders;

public class CmdRemove extends SubCommand {
    private final ChatEmojis plugin;

    public CmdRemove(ChatEmojis plugin, CmdEmoji command) {
        super(command, "remove");
        this.plugin = plugin;
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {
        // Define manager variables
        MessageManager msgM = this.plugin.getMessageManager();
        EmojiManager emojiM = this.plugin.getEmojiManager();
        ConfigurationSection section = emojiM.getEmojiSec();

        // Check if sender has permission
        if (!sender.hasPermission("chatemojis.remove")) {
            msgM.sendMessage(sender, "invalid-permission");
            return;
        }

        // Check if right amount of arguments were provided
        if (args.length == 1) {
            msgM.sendMessage(sender, "invalid-arguments");
            return;
        }

        // Define emoji name
        String emojiName = args[1].toLowerCase();

        // Check if the emoji exists
        if (!section.getKeys(false).contains(emojiName)) {
            msgM.sendMessage(sender, "emoji-doesnt-exist", StringPlaceholders.single("emoji", StringUtils.capitalize(emojiName)));
            return;
        }

        // Send emoji removed message
        msgM.sendMessage(sender, "removed-emoji", StringPlaceholders.single("emoji", emojiName));

        // Remove emoji
        emojiM.removeEmoji(emojiName);
    }

}