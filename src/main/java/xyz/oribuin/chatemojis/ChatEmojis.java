package xyz.oribuin.chatemojis;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.chatemojis.cmds.CmdEmojis;
import xyz.oribuin.chatemojis.events.EventPlayerChat;
import xyz.oribuin.chatemojis.hooks.PAPI;
import xyz.oribuin.chatemojis.hooks.PluginPlaceholders;
import xyz.oribuin.chatemojis.utils.TabComplete;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChatEmojis extends JavaPlugin {

    private static ChatEmojis instance;

    public static ChatEmojis getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        PluginManager pm = Bukkit.getPluginManager();

        getCommand("emojis").setExecutor(new CmdEmojis());
        getCommand("emojis").setTabCompleter(new TabComplete());
        pm.registerEvents(new EventPlayerChat(), this);

        if (pm.getPlugin("PlaceholderAPI") == null) {
            this.getLogger().warning("PlaceholderAPI is not installed, therefor PlaceholderAPI will not work.");
        }

        if (PAPI.enabled())
            new PluginPlaceholders().register();

        this.saveDefaultConfig();
        createFile("emojis.yml");
        createFile("messages.yml");

    }

    private void createFile(String fileName) {
        File file = new File(this.getDataFolder(), fileName);
        if (!file.exists()) {
            try (InputStream inStream = this.getResource(fileName)) {
                Files.copy(inStream, Paths.get(file.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
