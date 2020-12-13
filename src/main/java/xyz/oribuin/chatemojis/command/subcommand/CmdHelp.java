package xyz.oribuin.chatemojis.command.subcommand;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.util.HexUtils;
import xyz.oribuin.orilibrary.SubCommand;

public class CmdHelp extends SubCommand {
    private final ChatEmojis plugin;

    public CmdHelp(ChatEmojis plugin, CmdEmoji command) {
        super(command, "help");

        this.plugin = plugin;
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {
        MessageManager message = plugin.getManager(MessageManager.class);

        if (!sender.hasPermission("chatemojis.help")) {
            message.sendMessage(sender, "invalid-permission");
            return;
        }

        for (String line : message.getMessageConfig().getStringList("help-message")) {
            sender.sendMessage(HexUtils.colorify(line));
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 50f, 1f);
        }
    }

}