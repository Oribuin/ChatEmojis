package xyz.oribuin.chatemojis.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.orilibrary.command.SubCommand;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

import java.util.List;
import java.util.UUID;

@SubCommand.Info(
        names = {"toggle"},
        usage = "/emojis toggle",
        permission = "chatemojis.toggle"
)
public class SubToggle extends SubCommand {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();
    private final MessageManager msg = this.plugin.getManager(MessageManager.class);

    public SubToggle(ChatEmojis plugin, CmdEmoji command) {
        super(plugin, command);
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            this.msg.send(sender, "player-only");
            return;
        }

        final Player player = (Player) sender;

        final List<UUID> list = this.plugin.getToggleList();

        if (!list.remove(player.getUniqueId())) {
            list.add(player.getUniqueId());
        }

        this.msg.send(sender, "toggled-emojis", StringPlaceholders.single("toggled", list.contains(player.getUniqueId()) ? "Off" : "On"));
    }

}