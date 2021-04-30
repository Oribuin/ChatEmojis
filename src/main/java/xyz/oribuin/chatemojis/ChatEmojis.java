package xyz.oribuin.chatemojis;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
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
        final FileConfiguration config = this.getManager(MessageManager.class).getConfig();
        final String prefix = config.getString("prefix");
        final String playerOnly = prefix + config.getString("player-only");
        final String noPerm = prefix + config.getString("invalid-permission");

        // Register command
        new CmdEmoji(this).register(playerOnly, noPerm);

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
