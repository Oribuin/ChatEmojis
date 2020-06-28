package xyz.oribuin.chatemojis;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.chatemojis.cmds.CmdEmoji;
import xyz.oribuin.chatemojis.cmds.OriCommand;
import xyz.oribuin.chatemojis.hooks.PlaceholderExp;
import xyz.oribuin.chatemojis.hooks.VaultHook;
import xyz.oribuin.chatemojis.listeners.PlayerChat;
import xyz.oribuin.chatemojis.managers.ConfigManager;
import xyz.oribuin.chatemojis.managers.EmojiManager;
import xyz.oribuin.chatemojis.managers.MessageManager;

public class ChatEmojis extends JavaPlugin {

    private static ChatEmojis instance;
    private ConfigManager configManager;
    private EmojiManager emojiManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        instance = this;

        // Register commands
        this.registerCommands(new CmdEmoji(this));
        // Register Listeners
        this.registerListeners(new PlayerChat(this));

        // Register Managers
        this.configManager = new ConfigManager(this);
        this.emojiManager = new EmojiManager(this);
        this.messageManager = new MessageManager(this);

        // Register Vault
        VaultHook vaultHook = new VaultHook(this);
        vaultHook.setupEconomy();
        vaultHook.setupPermissions();

        // Register PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderExp(this);
        }

        this.saveDefaultConfig();
        this.reload();
    }

    public void reload() {
        this.configManager.reload();
        this.emojiManager.reload();
        this.messageManager.reload();
    }

    private void registerCommands(OriCommand... commands) {
        for (OriCommand cmd : commands) {
            cmd.registerCommand();
        }
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    public EmojiManager getEmojiManager() {
        return emojiManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public static ChatEmojis getInstance() {
        return instance;
    }
}
