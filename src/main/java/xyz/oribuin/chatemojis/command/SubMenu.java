package xyz.oribuin.chatemojis.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.menu.EmojiGUI;
import xyz.oribuin.orilibrary.command.SubCommand;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

@SubCommand.Info(
        names = {"menu"},
        usage = "/emojis menu <player>",
        permission = "chatemojis.player"
)
public class SubMenu extends SubCommand {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();

    public SubMenu(ChatEmojis plugin, CmdEmoji command) {
        super(plugin, command);
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {

        final MessageManager msg = this.plugin.getManager(MessageManager.class);

        if (args.length != 2) {
            msg.send(sender, "invalid-arguments", StringPlaceholders.single("usage", this.getInfo().usage()));
            return;
        }

        final Player player = Bukkit.getPlayer(args[1]);

        if (player == null) {
            msg.send(sender, "invalid-player");
            return;
        }

        new EmojiGUI(this.plugin, player);
    }

}