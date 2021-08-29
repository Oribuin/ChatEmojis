package xyz.oribuin.chatemojis.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.chatemojis.obj.Menu;
import xyz.oribuin.gui.Item;
import xyz.oribuin.gui.PaginatedGui;
import xyz.oribuin.orilibrary.util.FileUtils;
import xyz.oribuin.orilibrary.util.HexUtils;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmojiGUI extends Menu {

    public EmojiGUI(ChatEmojis plugin, Player player) {
        super(plugin, "emoji-menu");
        super.enable();
        final EmojiManager emojiManager = plugin.getManager(EmojiManager.class);

        // Check if they are using an old version of ChatEmojis.
        if (this.getMenuConfig().get("config-version") == null) {
            final File folder = new File(plugin.getDataFolder(), "menus");

            // Rename the old menu file so they dont lose it
            new File(folder, "emoji-menu.yml").renameTo(new File(folder, "old-menu.yml"));

            // Load the new config file.
            final File newFile = FileUtils.createFile(plugin, "menus", "emoji-menu.yml");
            this.setMenuConfig(YamlConfiguration.loadConfiguration(newFile));

        }

        List<Integer> pageSlots = new ArrayList<>();
        // If the player has a set of specific amount of page slots, Make them the page lsots.
        if (this.getMenuConfig().get("page-slots") != null)
            pageSlots = this.getMenuConfig().getIntegerList("page-slots");
        else if (this.getMenuConfig().get("page-range") != null) {

            // dear god
            // Check if they are using the menu range feature of the plugin.
            final List<Integer> pageRange = this.getMenuConfig().getIntegerList("page-range");

            // Make sure they have at least two numbers in there.
            if (pageRange.size() >= 2) {
                // for each number between start number and the last number added, add a page slot
                for (int i = pageRange.get(0); i < pageRange.get(pageRange.size() - 1); i++)
                    pageSlots.add(i);

            }
        } else {
            // Add the default page slots.
            for (int i = 0; i < 44; i++)
                pageSlots.add(i);
        }

        PaginatedGui gui = new PaginatedGui(this.getMenuConfig().getInt("gui-rows") * 9, this.getMenuConfig().getString("gui-name"), pageSlots);
        gui.setDefaultClickFunction(event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

            ((Player) event.getWhoClicked()).updateInventory();
        });

        // oh boy look at these variables to prevent an incredibly long line
        // Get the option from config and if the option doesn't exist, Use the default material, Gray stained glass panes.
        final String materialName = Optional.ofNullable(this.getMenuConfig().getString("filler")).orElse(Material.GRAY_STAINED_GLASS_PANE.name());
        final Material filler = Optional.ofNullable(Material.matchMaterial(materialName)).orElse(Material.GRAY_STAINED_GLASS_PANE);

        // Add all the page filler items into the gui.
        gui.setItems(this.getMenuConfig().getIntegerList("filler-slots"), Item.filler(filler).getItem(), event -> {});

        // Add the pagination items.
        gui.setItem(this.getMenuConfig().getInt("previous-page.slot"), this.getGUIItem("previous-page", null, player), event -> gui.previous(event.getWhoClicked()));
        gui.setItem(this.getMenuConfig().getInt("next-page.slot"), this.getGUIItem("next-page", null, player), event -> gui.next(event.getWhoClicked()));

        // Add any extra items the user wants in there.
        final ConfigurationSection section = this.getMenuConfig().getConfigurationSection("extra-items");
        if (section != null) {
            section.getKeys(false).forEach(s -> gui.setItem(section.getInt(s + ".slot"), this.getGUIItem("extra-items." + s, null, player), event -> clickEvent(player, "extra-items." + s, StringPlaceholders.empty())));
        }

        // Get the unlocked & locked message values.
        final String emojiUnlocked = this.getMenuConfig().getString("permission-status.unlocked");
        final String emojiLocked = this.getMenuConfig().getString("permission-status-locked");

        for (Emoji emoji : emojiManager.getCachedEmojis()) {

            if (getMenuConfig().getBoolean("display-accessed-only") && !emojiManager.canUseEmoji(player, emoji))
                continue;

            StringPlaceholders placeholders = StringPlaceholders.builder("emoji", emoji.getReplacement())
                    .addPlaceholder("check", emoji.getCheck())
                    .addPlaceholder("name", emoji.getName())
                    .addPlaceholder("id", emoji.getId())
                    .addPlaceholder("creator", emoji.getCreator() != null ? Bukkit.getOfflinePlayer(emoji.getCreator()).getName() : this.getMenuConfig().getString("no-creator"))
                    .addPlaceholder("permission", player.hasPermission("chatemojis.emoji." + emoji.getId().toLowerCase()) ?
                            HexUtils.colorify(emojiUnlocked != null ? emojiUnlocked : "Unlocked") : HexUtils.colorify(emojiLocked != null ? emojiLocked : "Locked"))
                    .build();

            gui.addPageItem(this.getGUIItem("emoji", emoji, player), event -> clickEvent(player, "emoji", placeholders));
        }

        gui.open(player);
    }


}
