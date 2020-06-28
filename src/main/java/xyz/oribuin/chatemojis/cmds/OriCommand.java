package xyz.oribuin.chatemojis.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import xyz.oribuin.chatemojis.ChatEmojis;

import java.util.List;

public abstract class OriCommand implements TabExecutor {

    public final ChatEmojis plugin;
    private final String commandName;

    public OriCommand(ChatEmojis plugin, String commandName) {
        this.plugin = plugin;
        this.commandName = commandName;
    }

    public void registerCommand() {
        PluginCommand cmd = Bukkit.getPluginCommand(this.commandName);

        if (cmd != null) {
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
    }

    // Register Command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }


    // Register command tab complete
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    // Get Command Name
    public String getName() {
        return commandName;
    }
}