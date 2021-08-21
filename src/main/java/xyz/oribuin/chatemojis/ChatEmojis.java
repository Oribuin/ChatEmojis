package xyz.oribuin.chatemojis;

import org.bukkit.Bukkit;
import xyz.oribuin.chatemojis.command.CmdEmoji;
import xyz.oribuin.chatemojis.hook.PAPI;
import xyz.oribuin.chatemojis.hook.PlaceholderExp;
import xyz.oribuin.chatemojis.hook.VaultHook;
import xyz.oribuin.chatemojis.listener.PlayerChat;
import xyz.oribuin.chatemojis.manager.ConfigManager;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.manager.MessageManager;
import xyz.oribuin.orilibrary.OriPlugin;

public class ChatEmojis extends OriPlugin {

    @Override
    public void enablePlugin() {

        // Register Managers
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            this.getManager(ConfigManager.class);
            this.getManager(EmojiManager.class);
            this.getManager(MessageManager.class);
        });

        // Register Vault
        VaultHook vaultHook = new VaultHook(this);
        vaultHook.setupEconomy();
        vaultHook.setupPermissions();

        // Get Command Messages.
        final MessageManager msg = this.getManager(MessageManager.class);

        // Register command
        new CmdEmoji(this).register(sender -> msg.send(sender, "player-only"), sender -> msg.send(sender, "invalid-permission"));

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerChat(this), this);

        // Register PlaceholderAPI
        if (PAPI.enabled()) {
            new PlaceholderExp(this).register();
        }
    }

    @Override
    public void disablePlugin() {

    }

}
