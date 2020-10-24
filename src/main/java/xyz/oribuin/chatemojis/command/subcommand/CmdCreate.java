package xyz.oribuin.chatemojis.command.subcommand;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.api.EmojiCreateEvent;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.command.SubCommand;
import xyz.oribuin.chatemojis.hook.VaultHook;
import xyz.oribuin.chatemojis.manager.ConfigManager;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.util.StringPlaceholders;

public class CmdCreate extends SubCommand {
    private final ChatEmojis plugin;

    public CmdCreate(ChatEmojis plugin, CmdEmoji command) {
        super(command, "create");

        this.plugin = plugin;
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {
        MessageManager msgM = this.plugin.getMessageManager();
        EmojiManager emojiM = this.plugin.getEmojiManager();
        ConfigurationSection section = emojiM.getEmojiSec();

        // Check if the user has enough permissions
        if (!sender.hasPermission("chatemojis.create")) {
            msgM.sendMessage(sender, "invalid-permission");
            return;
        }

        // Check if the sender has provided the right amount of arguments
        if (args.length < 4) {
            msgM.sendMessage(sender, "invalid-arguments");
            return;
        }

        // Define values
        String emojiName = args[1].toLowerCase();
        String emojiCheck = args[2].toLowerCase();
        String emojiReplacement = String.join(" ", args).substring(args[0].length() + emojiName.length() + emojiCheck.length() + 3);

        // Check if the emoji already exists
        if (section.getKeys(false).contains(emojiName)) {
            msgM.sendMessage(sender, "emoji-already-exists", StringPlaceholders.single("emoji", StringUtils.capitalize(emojiName)));
            return;
        }

        // Define the creation amount
        double createAmount = ConfigManager.Setting.CMD_SETTINGS_ECO_CREATE.getDouble();

        // Define the placeholders
        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addPlaceholder("emoji", StringUtils.capitalize(emojiName))
                .addPlaceholder("check", emojiCheck)
                .addPlaceholder("replacement", emojiReplacement)
                .addPlaceholder("money", createAmount).build();

        // If the sender is a player and economy is enabled
        if (sender instanceof Player && ConfigManager.Setting.CMD_SETTINGS_ECO_ENABLED.getBoolean()) {
            // define Economy, Permission and Player variable
            Economy eco = VaultHook.getVaultEco();
            Permission perm = VaultHook.getPermission();
            Player player = (Player) sender;

            // Check if the user has enough money for this.
            if (!eco.has(player, createAmount)) {
                msgM.sendMessage(sender, "invalid-funds");
                return;
            }

            // Add permission to player
            if (ConfigManager.Setting.CMD_SETTINGS_PERM_ENABLED.getBoolean()) {
                perm.playerAdd(player, "chatemojis.emoji." + emojiName);
            }

            // Take money from player
            eco.withdrawPlayer(player, createAmount);
            // Send money taken message
            msgM.sendMessage(sender, "money-taken", placeholders);
        }

        // Send created emoji message
        msgM.sendMessage(sender, "created-emoji", placeholders);

        // Create emoji in manager
        if (sender instanceof Player) {
            emojiM.createEmoji((Player) sender, emojiName, emojiCheck, emojiReplacement);
            Bukkit.getPluginManager().callEvent(new EmojiCreateEvent(emojiName, emojiCheck, emojiReplacement, (Player) sender));

        } else {
            emojiM.createEmoji(null, emojiName, emojiCheck, emojiReplacement);
            Bukkit.getPluginManager().callEvent(new EmojiCreateEvent(emojiName, emojiCheck, emojiReplacement, null));
        }

    }
}
