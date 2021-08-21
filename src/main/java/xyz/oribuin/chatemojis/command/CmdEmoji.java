package xyz.oribuin.chatemojis.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.menu.EmojiGUI;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.orilibrary.command.Command;
import xyz.oribuin.orilibrary.command.SubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Command.Info(
        name = "emojis",
        description = "Main command for ChatEmojis",
        permission = "chatemojis.use",
        playerOnly = false,
        usage = "/emojis",
        subCommands = {
                SubCreate.class,
                SubReload.class,
                SubRemove.class,
                SubMenu.class
        },
        aliases = {"chatemojis"}
)
public class CmdEmoji extends Command {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();
    private final MessageManager msg = this.plugin.getManager(MessageManager.class);
    private final List<Emoji> cachedEmojis = this.plugin.getManager(EmojiManager.class).getCachedEmojis();

    public CmdEmoji(ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void runFunction(CommandSender sender, String label, String[] args) {

        if (sender instanceof Player && args.length == 0) {
            new EmojiGUI(this.plugin, ((Player) sender));
            return;
        }

        this.runSubCommands(sender, args, x -> msg.send(x, "unknown-cmd"), x -> msg.send(x, "invalid-permission"));
    }

    @Override
    public List<String> completeString(CommandSender sender, String label, String[] args) {

        final List<String> tabComplete = new ArrayList<>();
        final List<Emoji> cachedEmojis = this.plugin.getManager(EmojiManager.class).getCachedEmojis();

        switch (args.length) {
            case 1: {
                this.getSubCommands().stream()
                        .map(SubCommand::getInfo)
                        .filter(info -> info.permission().length() != 0 && sender.hasPermission(info.permission()))
                        .forEach(info -> tabComplete.add(info.names()[0]));

                break;
            }

            case 2: {
                if (args[0].equalsIgnoreCase("create"))
                    tabComplete.add("name");

                if (args[0].equalsIgnoreCase("remove"))
                    tabComplete.addAll(cachedEmojis.stream().map(emoji -> emoji.getId().toLowerCase()).collect(Collectors.toList()));

                break;
            }

            case 3: {
                if (args[0].equalsIgnoreCase("create"))
                    tabComplete.add("<check>");

                break;
            }

            case 4: {
                if (args[0].equalsIgnoreCase("create"))
                    tabComplete.add("<emoji>");

                break;
            }
        }

        return tabComplete;
    }

}
