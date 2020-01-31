package xyz.oribuin.chatemojis;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.oribuin.chatemojis.cmds.CmdReload;
import xyz.oribuin.chatemojis.listeners.EmojiSend;
import xyz.oribuin.chatemojis.utils.Chat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ChatEmojis extends JavaPlugin {
    @Override
    public void onEnable() {

        /*
         * Variable Defining
         */

        PluginManager pm = Bukkit.getPluginManager();

        /*
         * Registering commands
         */

        getCommand("chatemojis").setExecutor(new CmdReload(this));

        /*
         * Registering events
         */

        pm.registerEvents(new EmojiSend(this), this);

        /*
         * Create the config.yml
         */

        this.saveDefaultConfig();

        /*
         * Create the emojis.yml
         */

        copyDefaultResource("emojis.yml");

        /*
         * Startup Message
         */

        this.getServer().getConsoleSender().sendMessage(Chat.cl(
                "\n\n&e******************\n" +
                        "\n&6Plugin: &f" + this.getDescription().getName() +
                        "\n&6Author: &f" + this.getDescription().getAuthors().get(0) +
                        "\n&6Version: &f" + this.getDescription().getVersion() +
                        "\n&6Website: &f" + this.getDescription().getWebsite() +
                        "\n\n&e******************"
        ));

    }

    public void copyDefaultResource(String fileName) {
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
