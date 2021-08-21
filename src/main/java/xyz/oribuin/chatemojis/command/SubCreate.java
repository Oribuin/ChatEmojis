package xyz.oribuin.chatemojis.command;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.hook.VaultHook;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.orilibrary.command.SubCommand;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

@SubCommand.Info(
        names = {"create"},
        usage = "/emojis create <name> <check> <emoji>",
        permission = "chatemojis.create"
)
public class SubCreate extends SubCommand {

    private final ChatEmojis plugin = (ChatEmojis) this.getOriPlugin();

    public SubCreate(final ChatEmojis plugin, final CmdEmoji command) {
        super(plugin, command);
    }

    @Override
    public void executeArgument(CommandSender sender, String[] args) {
        final MessageManager msg = this.plugin.getManager(MessageManager.class);
        final EmojiManager emojiManager = this.plugin.getManager(EmojiManager.class);

        // Check if the sender has provided the right amount of arguments
        if (args.length < 4) {
            msg.send(sender, "invalid-arguments", StringPlaceholders.single("usage", this.getInfo().usage()));
            return;
        }

        // Define values
        String emojiName = args[1];
        String emojiCheck = args[2].toLowerCase();
        String emojiReplacement = String.join(" ", args).substring(args[0].length() + emojiName.length() + emojiCheck.length() + 3);

        // Check if the emoji already exists
        if (emojiManager.getCachedEmojis().stream().anyMatch(emoji -> emoji.getId().equalsIgnoreCase(emojiName))) {
            msg.send(sender, "emoji-already-exists", StringPlaceholders.single("emoji", StringUtils.capitalize(emojiName)));
            return;
        }

        // Define the creation amount
        double createAmount = this.plugin.getConfig().getDouble("command-settings.create-price");

        // Define the placeholders
        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addPlaceholder("id", emojiName.toLowerCase())
                .addPlaceholder("emoji", emojiName)
                .addPlaceholder("check", emojiCheck)
                .addPlaceholder("replacement", emojiReplacement)
                .addPlaceholder("money", createAmount).build();

        // If the sender is a player and economy is enabled
        if (sender instanceof Player && this.plugin.getConfig().getBoolean("command-settings.economy")) {
            // define Economy, Permission and Player variable
            Economy eco = VaultHook.getVaultEco();
            Permission perm = VaultHook.getPermission();
            Player player = (Player) sender;

            // Check if the user has enough money for this.
            if (!eco.has(player, createAmount)) {
                msg.send(sender, "invalid-funds");
                return;
            }

            // Add permission to player
            if (plugin.getConfig().getBoolean("command-settings.add-perm")) {
                perm.playerAdd(player, "chatemojis.emoji." + emojiName);
            }

            // Take money from player
            eco.withdrawPlayer(player, createAmount);
            // Send money taken message
            msg.send(sender, "money-taken", placeholders);
        }

        // Send created emoji message
        msg.send(sender, "created-emoji", placeholders);

        final Emoji emoji = new Emoji(emojiName.toLowerCase(), emojiName, emojiCheck, emojiReplacement);
        emoji.setCreator(sender instanceof Player ? ((Player) sender).getUniqueId() : null);
        emojiManager.createEmoji(emoji);

    }

}
