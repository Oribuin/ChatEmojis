package xyz.oribuin.chatemojis.manager;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.hook.PAPI;
import xyz.oribuin.orilibrary.manager.Manager;
import xyz.oribuin.orilibrary.util.FileUtils;
import xyz.oribuin.orilibrary.util.HexUtils;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

import java.io.File;
import java.io.IOException;

public class MessageManager extends Manager {

    private final ChatEmojis plugin = (ChatEmojis) this.getPlugin();

    private FileConfiguration config;

    public MessageManager(ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void enable() {
        this.config = YamlConfiguration.loadConfiguration(FileUtils.createFile(this.plugin, "messages.yml"));

        // Set any values that dont exist
        for (Messages value : Messages.values()) {
            if (config.get(value.key) == null) {
                config.set(value.key, value.defaultValue);
            }
        }

        try {
            config.save(new File(plugin.getDataFolder(), "messages.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Send a configuration message without any placeholders
     *
     * @param receiver  The CommandSender who receives the message.
     * @param messageId The message path
     */
    public void send(CommandSender receiver, String messageId) {
        this.send(receiver, messageId, StringPlaceholders.empty());
    }

    /**
     * Send a configuration messageId with placeholders.
     *
     * @param receiver     The CommandSender who receives the messageId.
     * @param messageId    The messageId path
     * @param placeholders The Placeholders
     */
    public void send(CommandSender receiver, String messageId, StringPlaceholders placeholders) {
        final String msg = this.getConfig().getString(messageId);

        if (msg == null) {
            receiver.sendMessage(HexUtils.colorify("&c&lError &7| &fThis is an invalid message in the messages file, Please contact the server owner about this issue. (Id: " + messageId + ")"));
            return;
        }

        final String prefix = this.getConfig().getString("prefix");
        receiver.sendMessage(HexUtils.colorify(prefix + PAPI.apply(receiver instanceof Player ? (Player) receiver : null, placeholders.apply(msg))));
    }

    /**
     * Send a raw message to the receiver without any placeholders
     *
     * Use this to send a message to a player without the message being defined in a config.
     *
     * @param receiver The message receiver
     * @param message The raw message
     */
    public void sendRaw(CommandSender receiver, String message) {
        this.sendRaw(receiver, message, StringPlaceholders.empty());
    }

    /**
     * Send a raw message to the receiver with placeholders.
     *
     * Use this to send a message to a player without the message being defined in a config.
     *
     * @param receiver The message receiver
     * @param message The message
     * @param placeholders Message Placeholders.
     */
    public void sendRaw(CommandSender receiver, String message, StringPlaceholders placeholders) {
        receiver.sendMessage(HexUtils.colorify(PAPI.apply(receiver instanceof Player ? (Player) receiver : null, placeholders.apply(message))));
    }

    @Override
    public void disable() {

    }

    private enum Messages {
        PREFIX("prefix", "#9494edChatEmojis #cbcbd3» "),
        CREATED_EMOJI("created-emoji", "&bYou have created the emoji &e%replacement%&b! Use this by typing &e%check%&b!"),
        MONEY_TAKEN("money-taken", "&bYou have bought the emoji &e%emoji&b for $&e%money%&b!"),
        REMOVED_EMOJI("removed-emoji", "&bYou have deleted the emoji &e%emoji%&b!"),
        OPENED_MENU("opened-menu", "&bYou have opened the menu for &e%player%&b!"),

        RELOAD("reload", "&bYou have reloaded ChatEmojis"),
        INVALID_PERMISSION("invalid-permission", "&cYou do not have permission to execute this command."),
        INVALID_PLAYER("invalid-player", "&cPlease enter a valid player."),
        INVALID_ARGUMENTS("invalid-arguments", "&cPlease provide valid arguments."),
        INVALID_FUNDS("invalid-funds", "&cYou do not have enough funds to do this."),
        EMOJI_ALREADY_EXISTS("emoji-already-exists", "&cThe emoji, %emoji%, already exists."),
        EMOJI_DOESNT_EXIST("emoji-doesnt-exist", "&cThe emoji, %emoji%, does not exist."),
        UNKNOWN_COMMAND("unknown-command", "&cPlease include a valid command."),
        PLAYER_ONLY("player-only", "&cOnly a player can execute this command.");

        private final String key;
        private final Object defaultValue;

        Messages(final String key, final Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

    }

}
