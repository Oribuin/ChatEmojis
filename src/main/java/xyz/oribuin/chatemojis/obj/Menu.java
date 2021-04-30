package xyz.oribuin.chatemojis.obj;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.hook.PAPI;
import xyz.oribuin.orilibrary.util.FileUtils;
import xyz.oribuin.orilibrary.util.HexUtils;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Menu {
    private final ChatEmojis plugin;
    private final String guiName;

    private FileConfiguration menuConfig;

    public Menu(ChatEmojis plugin, String guiName) {
        this.plugin = plugin;
        this.guiName = guiName;

    }

    public void enable() {
        FileUtils.createMenuFile(plugin, guiName);
        this.menuConfig = YamlConfiguration.loadConfiguration(this.getMenuFile());
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
                    .build();
        }

        // Create the lore
        final List<String> lore = new ArrayList<>();
        StringPlaceholders finalPlaceholders = placeholders;
        config.getStringList(path + ".lore").forEach(s -> lore.add(format(s, player, finalPlaceholders)));

        // Create the item builder
        final ItemBuilder item = ItemBuilder.from(Material.valueOf(config.getString(path + ".material")))
                .setName(format(config.getString(path + ".name"), player, finalPlaceholders))
                .setLore(lore)
                .setAmount(config.getInt(path + ".amount"))
                .glow(config.getBoolean(path + ".glow"));

        // Define the texture String
        final String texture = config.getString(path + ".texture");

        // Check if can apply texture
        if (texture != null) {
            item.setSkullTexture(texture);
        }

        ItemStack itemStack = item.build();

        if (config.get(path + ".model") != null && config.getInt(path + ".model") != -1) {
            itemStack = NBTEditor.set(itemStack, config.getInt(path + ".model"), "CustomModelData");
        }

        // Build the new itemstack
        return itemStack;
    }

    public String format(String string, Player player, StringPlaceholders placeholders) {
        return HexUtils.colorify(PAPI.apply(player, placeholders.apply(string)));
    }

    public GuiItem fillerItem() {
        return ItemBuilder.from(this.getGUIItem("filler", null, null)).asGuiItem();
    }

    public ChatEmojis getPlugin() {
        return plugin;
    }
}
