package xyz.oribuin.chatemojis.manager;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.hook.PlaceholderAPIHook;
import xyz.oribuin.chatemojis.util.FileUtils;
import xyz.oribuin.chatemojis.util.HexUtils;
import xyz.oribuin.chatemojis.util.StringPlaceholders;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MessageManager extends Manager {
    private static FileConfiguration messageConfig;

    public MessageManager(ChatEmojis plugin) {
        super(plugin);
    }

    @Override
    public void reload() {
        FileUtils.createFile(this.plugin, "messages.yml");
        messageConfig = YamlConfiguration.loadConfiguration(new File(this.plugin.getDataFolder(), "messages.yml"));

        for (Setting value : Setting.values()) {
            if (messageConfig.get(value.key) == null) {
                messageConfig.set(value.key, value.defaultValue);
            }

            value.load(messageConfig);
        }

        try {
            messageConfig.save(new File(plugin.getDataFolder(), "messages.yml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void sendMessage(CommandSender sender, String messageId) {
        this.sendMessage(sender, messageId, StringPlaceholders.empty());
    }

    public void sendMessage(CommandSender sender, String messageId, StringPlaceholders placeholders) {

        if (messageConfig.getString(messageId) == null) {
            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify("{#ff4072}" + messageId + " is null in messages.yml")));
            return;
        }

        if (!messageConfig.getString(messageId).isEmpty()) {
            final String msg = messageConfig.getString("prefix") + placeholders.apply(messageConfig.getString(messageId));

            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify(this.parsePlaceholders(sender, msg))));
        }
    }

    public FileConfiguration getMessageConfig() {
        return messageConfig;
    }

    private String parsePlaceholders(CommandSender sender, String message) {
        if (sender instanceof Player)
            return PlaceholderAPIHook.apply((Player) sender, message);

        return message;
    }

    public enum Setting {
        PREFIX("prefix", "#9494edChatEmojis #cbcbd3» "),
        CREATED_EMOJI("created-emoji", "&bYou have created the emoji &e%replacement%&b! Use this by typing &e%check%&b!"),
        MONEY_TAKEN("money-taken", "&bYou have bought the emoji &e%emoji&b for $&e%money%&b!"),
        REMOVED_EMOJI("removed-emoji", "&bYou have deleted the emoji &e%emoji%&b!"),
        OPENED_MENU("opened-menu", "&bYou have opened the menu for &e%player%&b!"),

        HELP_MESSAGE("help-message", Arrays.asList(
                " ",
                " <rainbow:0.7>ChatEmojis &f» &bCommands",
                " &f• &b/emojis help &e- &bShow this message.",
                " &f• &b/emojis create &e<Name> <Keyword> <Emoji> &f- &bCreate an emoji.",
                " &f• &b/emojis remove &e<Keyword> &f- &bRemove an emoji.",
                " &f• &b/emojis menu &e[Player] &f- &bOpen the emoji menu.",
                " &f• &b/emojis reload &f- &bReload the plugin.",
                " ",
                " &f» &bPlugin created by <g:#4776E6:#8E54E9>Oribuin",
                " "
        )),

        RELOAD("reload", "&bYou have reloaded ChatEmojis (&e%version%&b)"),
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
        private Object value = null;

        Setting(String key, Object defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }


        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();

            return (boolean) this.value;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();

            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();

            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();

            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();

            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();

            return (String) this.value;
        }

        private double getNumber() {
            this.loadValue();

            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();

            return (List<String>) this.value;
        }

        /**
         * Loads the value from the config and caches it
         */
        private void load(FileConfiguration config) {
            this.value = config.get(this.key);
        }

        private void loadValue() {
            if (this.value != null)
                return;

            this.value = ChatEmojis.getInstance().getConfig().get(this.key);

            if (this.value == null) {
                ChatEmojis.getInstance().getConfig().set(this.key, this.defaultValue);
            }
        }
    }
}
