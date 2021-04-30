package xyz.oribuin.chatemojis.command.subcommand;

import org.bukkit.command.CommandSender;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.orilibrary.command.SubCommand;
import xyz.oribuin.orilibrary.libs.jetbrains.annotations.NotNull;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

@SubCommand.Info(
        names = {"reload"},
        usage = "/emojis reload",
        permission = "chatemojis.reload",
        command = CmdEmoji.class
)
public class SubReload extends SubCommand {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();

    public SubReload(ChatEmojis plugin, CmdEmoji command) {
        super(plugin, command);
    }

    @Override
    public void executeArgument(@NotNull CommandSender sender, @NotNull String[] args) {

        final MessageManager msg = this.plugin.getManager(MessageManager.class);
        final EmojiManager emojiManager = this.plugin.getManager(EmojiManager.class);

        // Reload main config files
        emojiManager.disable();
        emojiManager.enable();

        msg.disable();
        msg.enable();

        msg.send(sender, "reload", StringPlaceholders.single("version", "1.3.0"));
    }

}