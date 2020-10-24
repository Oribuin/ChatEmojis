package xyz.oribuin.chatemojis;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.hook.PlaceholderAPIHook;
import xyz.oribuin.chatemojis.hook.PlaceholderExp;
import xyz.oribuin.chatemojis.hook.VaultHook;
import xyz.oribuin.chatemojis.listener.PlayerChat;
import xyz.oribuin.chatemojis.manager.ConfigManager;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.GuiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;

public class ChatEmojis extends JavaPlugin {

    private static ChatEmojis instance;
    private ConfigManager configManager;
    private EmojiManager emojiManager;
    private GuiManager guiManager;
    private MessageManager messageManager;

    public static ChatEmojis getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        // Register Managers
        this.configManager = new ConfigManager(this);
        this.emojiManager = new EmojiManager(this);
        this.guiManager = new GuiManager(this);
        this.messageManager = new MessageManager(this);
        // Register Vault
        VaultHook vaultHook = new VaultHook(this);
        vaultHook.setupEconomy();
        vaultHook.setupPermissions();


        // Register command
        new CmdEmoji(this).register();

        // Register Listeners
        this.registerListeners(new PlayerChat(this));

        // Register PlaceholderAPI
        if (PlaceholderAPIHook.enabled()) {
            new PlaceholderExp(this).register();
        }

        this.guiManager.registerMenus();
        this.saveDefaultConfig();
        this.reload();
    }

    public void reload() {
        this.configManager.reload();
        this.emojiManager.reload();
        this.guiManager.reload();
        this.messageManager.reload();
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
}
