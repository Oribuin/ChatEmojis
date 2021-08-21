package xyz.oribuin.chatemojis.command;

import org.bukkit.command.CommandSender;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.orilibrary.command.SubCommand;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

@SubCommand.Info(
        names = {"reload"},
        usage = "/emojis reload",
        permission = "chatemojis.reload"
)
public class SubReload extends SubCommand {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();

    public SubReload(ChatEmojis plugin, CmdEmoji command) {
        super(plugin, command);
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {

        final MessageManager msg = this.plugin.getManager(MessageManager.class);

        this.plugin.reload();
        msg.send(sender, "reload", StringPlaceholders.single("version", this.plugin.getDescription().getVersion()));
    }

}