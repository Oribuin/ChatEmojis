package xyz.oribuin.chatemojis.command;

import org.bukkit.command.CommandSender;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.orilibrary.command.SubCommand;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

import java.util.List;

@SubCommand.Info(
        names = {"remove"},
        usage = "/emojis remove <name> ",
        permission = "chatemojis.remove"
)
public class SubRemove extends SubCommand {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();

    public SubRemove(ChatEmojis plugin, CmdEmoji command) {
        super(plugin, command);
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {

        final MessageManager msg = this.plugin.getManager(MessageManager.class);

        if (args.length != 2) {
            msg.send(sender, "invalid-arguments", StringPlaceholders.single("usage", this.getInfo().usage()));
            return;
        }

        final String name = args[1];
        final List<Emoji> cachedEmojis = this.plugin.getManager(EmojiManager.class).getCachedEmojis();

        if (cachedEmojis.stream().noneMatch(x -> x.getId().equalsIgnoreCase(name))) {
            msg.send(sender, "emoji-doesnt-exist");
            return;
        }


        this.plugin.getManager(EmojiManager.class).deleteEmoji(name);
        msg.send(sender, "removed-emoji", StringPlaceholders.single("emoji", name.toLowerCase()));

    }

}