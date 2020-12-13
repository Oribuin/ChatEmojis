package xyz.oribuin.chatemojis.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.subcommand.*;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.orilibrary.OriCommand;
import xyz.oribuin.orilibrary.SubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CmdEmoji extends OriCommand {
    private final List<SubCommand> subcommands = new ArrayList<>();

    public CmdEmoji(ChatEmojis plugin) {
        super(plugin, "chatemoji");
    }

    public void executeCommand(CommandSender sender, String[] args) {

        for (SubCommand cmd : subcommands) {
            if (args.length == 0) {
                plugin.getManager(MessageManager.class).sendMessage(sender, "unknown-command");
                break;
            }

            if (Arrays.asList(cmd.getNames()).contains(args[0].toLowerCase())) {
                cmd.executeArgument(sender, args);
                break;
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (!getCommand().getName().equalsIgnoreCase(this.getName()))
            return Collections.emptyList();

        List<String> suggestions = new ArrayList<>();
        if (args.length == 0 || args.length == 1) {
            String subCommand = args.length == 0 ? "" : args[0];
            List<String> commands = new ArrayList<>();

            if (sender.hasPermission("chatemojis.create"))
                commands.add("create");

            if (sender.hasPermission("chatemojis.menu"))
                commands.add("menu");

            if (sender.hasPermission("chatemojis.reload"))
                commands.add("reload");

            if (sender.hasPermission("chatemojis.remove"))
                commands.add("remove");

            StringUtil.copyPartialMatches(subCommand, commands, suggestions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create") && sender.hasPermission("chatemojis.create")) {
                StringUtil.copyPartialMatches(args[1].toLowerCase(), Collections.singletonList("<name>"), suggestions);

            } else if (args[0].equalsIgnoreCase("menu") && sender.hasPermission("chatemojis.menu.other")) {
                List<String> players = new ArrayList<>();

                Bukkit.getOnlinePlayers().stream().filter(player -> !player.hasMetadata("vanished")).forEachOrdered(player -> players.add(player.getName()));

                StringUtil.copyPartialMatches(args[1].toLowerCase(), players, suggestions);
            } else if (args[0].equalsIgnoreCase("remove") && sender.hasPermission("chatemojis.remove")) {
                List<String> emojiNames = new ArrayList<>();

                this.plugin.getManager(EmojiManager.class).getEmojiSec().getKeys(false).forEach(emoji -> emojiNames.add(StringUtils.capitalize(emoji)));

                StringUtil.copyPartialMatches(args[1].toLowerCase(), emojiNames, suggestions);
            }


        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create") && sender.hasPermission("chatemojis.create")) {
                StringUtil.copyPartialMatches(args[2].toLowerCase(), Collections.singletonList("<check>"), suggestions);

            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("create") && sender.hasPermission("chatemojis.create")) {
                StringUtil.copyPartialMatches(args[3].toLowerCase(), Collections.singletonList("<emoji>"), suggestions);
            }
        } else {
            return null;
        }

        return suggestions;
    }

    @Override
    public void addSubCommands() {

        ChatEmojis chatEmojis = (ChatEmojis) plugin;

        subcommands.addAll(Arrays.asList(new CmdCreate(chatEmojis, this),
                new CmdHelp(chatEmojis, this),
                new CmdMenu(chatEmojis, this),
                new CmdReload(chatEmojis, this),
                new CmdRemove(chatEmojis, this)
        ));
    }
}
