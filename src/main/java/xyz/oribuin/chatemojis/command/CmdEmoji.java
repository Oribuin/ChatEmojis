package xyz.oribuin.chatemojis.command;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.subcommand.SubCreate;
import xyz.oribuin.chatemojis.command.subcommand.SubMenu;
import xyz.oribuin.chatemojis.command.subcommand.SubReload;
import xyz.oribuin.chatemojis.command.subcommand.SubRemove;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.menu.EmojiGUI;
import xyz.oribuin.orilibrary.command.Command;
import xyz.oribuin.orilibrary.libs.jetbrains.annotations.NotNull;

@Command.Info(
        name = "emojis",
        description = "Main command for ChatEmojis",
        permission = "chatemojis.use",
        playerOnly = false,
        usage = "/emojis",
        subcommands = {SubCreate.class, SubReload.class, SubRemove.class, SubMenu.class},
        aliases = {"chatemojis"}
)
public class CmdEmoji extends Command {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();

    public CmdEmoji(ChatEmojis plugin) {
        super((plugin));
    }

    @Override
    public void runFunction(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player && args.length == 0) {
            new EmojiGUI(this.plugin).createGUI(((Player) sender));
            return;
        }

        // TODO, Delete
        if (args.length == 1 && args[0].equalsIgnoreCase("nbt")) {
            final Player player = (Player) sender;
            player.sendMessage(NBTEditor.getKeys(player.getInventory().getItemInMainHand()).toString());
            return;
        }

        final FileConfiguration config = this.plugin.getManager(MessageManager.class).getConfig();
        final String prefix = config.getString("prefix");
        final String unknownCommand = prefix + config.getString("unknown-command");
        final String noPerm = prefix + config.getString("invalid-permission");
        this.runSubCommands(sender, args, unknownCommand, noPerm);
    }

}
