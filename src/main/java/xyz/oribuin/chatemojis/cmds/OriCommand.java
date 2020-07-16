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

    public abstract void executeCommand(CommandSender sender, String[] args);

    // Register Command
     public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        this.executeCommand(sender, args);
        return true;
    }


    // Register command tab complete
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        return null;
    }

    // Get Command Name
    public String getName() {
        return commandName;
    }

    public Command getCommand() {
         return plugin.getCommand(this.getName());
    }
}