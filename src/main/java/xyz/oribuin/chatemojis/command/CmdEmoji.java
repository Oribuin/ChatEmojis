package xyz.oribuin.chatemojis.command;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.subcommand.SubCreate;
import xyz.oribuin.chatemojis.command.subcommand.SubMenu;
import xyz.oribuin.chatemojis.command.subcommand.SubReload;
import xyz.oribuin.chatemojis.command.subcommand.SubRemove;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.menu.EmojiGUI;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.orilibrary.command.Command;
import xyz.oribuin.orilibrary.command.SubCommand;
import xyz.oribuin.orilibrary.libs.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        final FileConfiguration config = this.plugin.getManager(MessageManager.class).getConfig();
        final String prefix = config.getString("prefix");
        final String unknownCommand = prefix + config.getString("unknown-command");
        final String noPerm = prefix + config.getString("invalid-permission");
        this.runSubCommands(sender, args, unknownCommand, noPerm);
    }

    @Override
    public @NotNull List<String> completeString(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

        final List<String> tabComplete = new ArrayList<>();
        final List<Emoji> cachedEmojis = this.plugin.getManager(EmojiManager.class).getCachedEmojis();

        if (this.getAnnotation().permission().length() > 0 && !sender.hasPermission(this.getAnnotation().permission()))
            return playerList(sender);

        switch (args.length) {
            case 1: {
                this.getSubCommands().forEach(subCommand -> {
                    final SubCommand.Info info = subCommand.getAnnotation();
                    if (info.permission().length() != 0 && sender.hasPermission(info.permission())) tabComplete.add(info.names()[0].toLowerCase());
                });

                break;
            }

            case 2: {
                if (args[0].equalsIgnoreCase("create")) {
                    tabComplete.add("<name>");
                    break;
                }

                if (args[0].equalsIgnoreCase("remove")) {
                    tabComplete.addAll(cachedEmojis.stream().map(emoji -> emoji.getId().toLowerCase()).collect(Collectors.toList()));
                    break;
                }

                return playerList(sender);
            }

            case 3: {
                if (args[0].equalsIgnoreCase("create")) tabComplete.add("<check>");

                else return playerList(sender);

                break;
            }

            case 4: {
                if (args[0].equalsIgnoreCase("create")) tabComplete.add("<emoji>");

                else return playerList(sender);

                break;
            }

            default:
                return playerList(sender);
        }

        return tabComplete;
    }

}
