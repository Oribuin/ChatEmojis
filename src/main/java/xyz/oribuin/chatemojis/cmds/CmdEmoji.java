package xyz.oribuin.chatemojis.cmds;

import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.guis.MainMenu;
import xyz.oribuin.chatemojis.hooks.VaultHook;
import xyz.oribuin.chatemojis.managers.ConfigManager;
import xyz.oribuin.chatemojis.managers.EmojiManager;
import xyz.oribuin.chatemojis.managers.MessageManager;
import xyz.oribuin.chatemojis.utils.HexUtils;
import xyz.oribuin.chatemojis.utils.StringPlaceholders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdEmoji extends OriCommand {

    public CmdEmoji(ChatEmojis plugin) {
        super(plugin, "chatemoji");
    }

    public void onCreateCommand(CommandSender sender, String[] args) {
        MessageManager msgM = this.plugin.getMessageManager();
        EmojiManager emojiM = this.plugin.getEmojiManager();
        ConfigurationSection section = emojiM.getEmojiSec();

        // Check permission
        if (!sender.hasPermission("chatemojis.create")) {
            msgM.sendMessage(sender, "invalid-permission");
            return;
        }

        // Check args
        if (args.length < 4) {
            msgM.sendMessage(sender, "invalid-arguments");
            return;
        }

        String emojiName = args[1].toLowerCase();
        String emojiCheck = args[2].toLowerCase();
        String emojiReplacement = String.join(" ", args).substring(args[0].length() + emojiName.length() + emojiCheck.length() + 3);

        if (section.getKeys(false).contains(emojiName)) {
            msgM.sendMessage(sender, "emoji-already-exists", StringPlaceholders.single("emoji", StringUtils.capitalize(emojiName)));
            return;
        }

        double createAmount = ConfigManager.Setting.CMD_SETTINGS_ECO_CREATE.getDouble();

        StringPlaceholders placeholders = StringPlaceholders.builder()
                .addPlaceholder("emoji", StringUtils.capitalize(emojiName))
                .addPlaceholder("check", emojiCheck)
                .addPlaceholder("replacement", emojiReplacement)
                .addPlaceholder("money", createAmount).build();

        // Check economy
        if (sender instanceof Player && ConfigManager.Setting.CMD_SETTINGS_ECO_ENABLED.getBoolean()) {
            Economy eco = VaultHook.getVaultEco();
            Permission perm = VaultHook.getPermission();
            Player player = (Player) sender;

            if (!eco.has(player, createAmount)) {
                msgM.sendMessage(sender, "invalid-funds");
                return;
            }

            // Add permission to player
            if (ConfigManager.Setting.CMD_SETTINGS_PERM_ENABLED.getBoolean()) {
                perm.playerAdd(player, "chatemojis.emoji." + emojiName);
            }

            eco.withdrawPlayer(player, createAmount);
            msgM.sendMessage(sender, "money-taken", placeholders);
        }

        msgM.sendMessage(sender, "created-emoji", placeholders);
        if (sender instanceof Player)
            emojiM.createEmoji((Player) sender, emojiName, emojiCheck, emojiReplacement);
        else
            emojiM.createEmoji(null, emojiName, emojiCheck, emojiReplacement);
    }

    private void onReloadCommand(CommandSender sender) {
        MessageManager msgM = this.plugin.getMessageManager();
        if (!sender.hasPermission("chatemojis.reload")) {
            msgM.sendMessage(sender, "invalid-permission");
            return;
        }

        this.plugin.reload();
        msgM.sendMessage(sender, "reload", StringPlaceholders.single("version", this.plugin.getDescription().getVersion()));
    }

    private void onRemoveCommand(CommandSender sender, String[] args) {
        MessageManager msgM = this.plugin.getMessageManager();
        EmojiManager emojiM = this.plugin.getEmojiManager();
        ConfigurationSection section = emojiM.getEmojiSec();

        // Check permission
        if (!sender.hasPermission("chatemojis.remove")) {
            msgM.sendMessage(sender, "invalid-permission");
            return;
        }

        // Check args
        if (args.length == 1) {
            msgM.sendMessage(sender, "invalid-arguments");
            return;
        }

        String emojiName = args[1].toLowerCase();

        if (!section.getKeys(false).contains(emojiName)) {
            msgM.sendMessage(sender, "emoji-doesnt-exist", StringPlaceholders.single("emoji", StringUtils.capitalize(emojiName)));
            return;
        }

        msgM.sendMessage(sender, "removed-emoji", StringPlaceholders.single("emoji", emojiName));
        emojiM.removeEmoji(emojiName);
    }

    public void executeCommand(CommandSender sender, String[] args) {
        MessageManager msgM = this.plugin.getMessageManager();

        if (!this.getCommand().getName().equals(this.getName()))
            return;

        if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("menu")) {
            if (!(sender instanceof Player)) {
                msgM.sendMessage(sender, "player-only");
                return;
            }

            if (!(sender.hasPermission("chatemojis.menu"))) {
                msgM.sendMessage(sender, "invalid-permission");
                return;
            }

            Player player = (Player) sender;
            new MainMenu(plugin, player).openGui();
            return;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("menu")) {
            Player mentioned = Bukkit.getPlayer(args[1]);

            if (!(sender.hasPermission("chatemojis.menu.other"))) {
                msgM.sendMessage(sender, "invalid-permission");
                return;
            }

            // Check if mentioned is null, offline, vanished
            if (mentioned == null || !mentioned.isOnline() || mentioned.hasMetadata("vanished")) {
                // Send invalid player
                msgM.sendMessage(sender, "invalid-player");
                return;
            }


            new MainMenu(plugin, mentioned).openGui();
            sender.spigot().sendMessage(TextComponent.fromLegacyText(HexUtils.colorify(("{#ff4072}Opened menu for " + mentioned.getName()))));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                this.onCreateCommand(sender, args);
                break;
            case "remove":
                this.onRemoveCommand(sender, args);
                break;
            case "reload":
                this.onReloadCommand(sender);
                break;
            default:
                msgM.sendMessage(sender, "unknown-command");
        }

        return;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase(this.getName()))
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

                this.plugin.getEmojiManager().getEmojiSec().getKeys(false).forEach(emoji -> emojiNames.add(StringUtils.capitalize(emoji)));

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
}
