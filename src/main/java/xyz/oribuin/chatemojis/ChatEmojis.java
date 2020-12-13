package xyz.oribuin.chatemojis;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.hook.PlaceholderAPIHook;
import xyz.oribuin.chatemojis.hook.PlaceholderExp;
import xyz.oribuin.chatemojis.hook.VaultHook;
import xyz.oribuin.chatemojis.listener.PlayerChat;
import xyz.oribuin.chatemojis.manager.ConfigManager;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.GuiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.orilibrary.OriPlugin;

public class ChatEmojis extends OriPlugin {

    private static ChatEmojis instance;

    public static ChatEmojis getInstance() {
        return instance;
    }

    @Override
    public void enablePlugin() {
        instance = this;

        // Register Managers
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            this.getManager(ConfigManager.class);
            this.getManager(EmojiManager.class);
            this.getManager(GuiManager.class);
            this.getManager(MessageManager.class);
        });

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
    }

    @Override
    public void disablePlugin() {

    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
