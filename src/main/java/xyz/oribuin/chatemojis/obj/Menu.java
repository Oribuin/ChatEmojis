package xyz.oribuin.chatemojis.obj;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.action.Action;
import xyz.oribuin.chatemojis.action.type.*;
import xyz.oribuin.chatemojis.hook.PAPI;
import xyz.oribuin.gui.Item;
import xyz.oribuin.orilibrary.util.FileUtils;
import xyz.oribuin.orilibrary.util.HexUtils;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Menu {
    private final ChatEmojis plugin;
    private final String guiName;

    private final List<Action> actions = Arrays.asList(new BroadcastAction(), new CloseAction(), new CommandAction(), new ConsoleAction(), new MessageAction(), new SoundAction());

    private File file;
    private FileConfiguration menuConfig;

    public Menu(ChatEmojis plugin, String guiName) {
        this.plugin = plugin;
        this.guiName = guiName;

    }

    public void enable() {
        this.file = FileUtils.createFile(plugin, "menus", guiName + ".yml");
        this.menuConfig = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getMenuConfig() {
        return menuConfig;
    }

    public String getGuiName() {
        return guiName.toLowerCase();
    }

    private File getMenuFile() {
        return new File(this.plugin.getDataFolder() + File.separator + "menus", this.getGuiName() + ".yml");
    }

    /**
     * Get an itemstack based on a configuration path.
     *
     * @param path   The config path
     * @param emoji  A potential emoji for StringPlaceholders
     * @param player The player
     * @return The itemstack formed from the gui
     */
    public ItemStack getGUIItem(final String path, final @Nullable Emoji emoji, Player player) {
        final FileConfiguration config = this.getMenuConfig();

        StringPlaceholders placeholders = StringPlaceholders.empty();

        // Define emoji placeholders if the emoji isnt null
        if (emoji != null) {
            placeholders = StringPlaceholders.builder("emoji", emoji.getReplacement())
                    .addPlaceholder("check", emoji.getCheck())
                    .addPlaceholder("name", emoji.getName())
                    .addPlaceholder("id", emoji.getId())
                    .addPlaceholder("creator", emoji.getCreator() != null ? Bukkit.getOfflinePlayer(emoji.getCreator()).getName() : this.getMenuConfig().getString("no-creator"))
                    .addPlaceholder("permission", player.hasPermission("chatemojis.emoji." + emoji.getId().toLowerCase()) ?
                            HexUtils.colorify(this.getMenuConfig().getString("permission-status.unlocked")) : HexUtils.colorify(this.getMenuConfig().getString("permission-status.locked")))
                    .build();

        }

        // Create the lore
        final List<String> lore = new ArrayList<>();
        StringPlaceholders finalPlaceholders = placeholders;
        config.getStringList(path + ".lore").forEach(s -> lore.add(format(s, player, finalPlaceholders)));

        // Create the item builder
        final Item.Builder item = new Item.Builder(Material.valueOf(config.getString(path + ".material")))
                .setName(format(config.getString(path + ".name"), player, finalPlaceholders))
                .setLore(lore)
                .setAmount(config.getInt(path + ".amount"))
                .glow(config.getBoolean(path + ".glow"));

        // Define the texture String
        final String texture = config.getString(path + ".texture");

        // Check if can apply texture
        if (texture != null) {
            item.setTexture(texture);
        }

        if (config.get(path + ".model") != null && config.getInt(path + ".model") != -1) {
            item.setNBT(plugin, "CustomModelData", config.getInt(path + ".model"));
        }

        // Build the new itemstack
        return item.create();
    }

    /**
     * Run a function from items with specific functionalities based on the actions
     *
     * @param player       The player
     * @param path         The path to the GUI Item
     * @param placeholders Any String Placeholders.
     */
    public void clickEvent(Player player, String path, StringPlaceholders placeholders) {

        // yes this is scuffed, i am aware
        this.getMenuConfig().getStringList(path + ".actions").forEach(s -> {

            for (Action action : this.actions) {
                if (s.startsWith("[" + action.actionType() + "]")) {
                    int requiredLength = 2 + action.actionType().length();

                    final String msg = s.substring(requiredLength);
                    action.executeAction(plugin, player, HexUtils.colorify(PAPI.apply(player, placeholders.apply(msg))));
                }
            }

        });

    }

    /**
     * Format a string with PAPI Placeholders, Normal String placeholders and colorize it
     *
     * @param string       The string
     * @param player       The player
     * @param placeholders Any potential String
     * @return string formatted.
     */
    public String format(String string, Player player, StringPlaceholders placeholders) {
        return HexUtils.colorify(PAPI.apply(player, placeholders.apply(string)));
    }

    public void setMenuConfig(FileConfiguration menuConfig) {
        this.menuConfig = menuConfig;
    }

}
