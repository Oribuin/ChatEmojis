package xyz.oribuin.chatemojis.command;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private final OriCommand command;
    private final String[] names;

    public SubCommand(OriCommand command, String... names) {
        this.command = command;
        this.names = names;
    }

    /**
     * Command execution function
     */
    public abstract void executeArgument(CommandSender sender, String[] args);

    /**
     * Get the main command
     *
     * @return Main plugin command
     */
    public OriCommand getCommand() {
        return command;
    }

    /**
     * The list of arguments that could be used to execute the function
     *
     * @return String[] of names
     */
    public String[] getNames() {
        return names;
    }
}
