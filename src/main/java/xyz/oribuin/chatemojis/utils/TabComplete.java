package xyz.oribuin.chatemojis.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("chatemojis.create"))
                commands.add("create");
            if (sender.hasPermission("chatemojis.reload"))
                commands.add("reload");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create"))
                commands.add("<name>");

            StringUtil.copyPartialMatches(args[1], commands, completions);
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                commands.add("<check>");
                StringUtil.copyPartialMatches(args[2], commands, completions);
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("create")) {
                commands.add("<emoji>");
                StringUtil.copyPartialMatches(args[3], commands, completions);
            }
        }

        Collections.sort(completions);
        return completions;
    }
}
