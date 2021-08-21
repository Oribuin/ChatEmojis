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

        if (this.getMenuConfig().get("config-version") == null) {
            final File folder = new File(plugin.getDataFolder(), "menus");

            new File(folder, "emoji-menu.yml").renameTo(new File(folder, "old-menu.yml"));

            final File newFile = FileUtils.createFile(plugin, "menus", "emoji-menu.yml");
            this.setMenuConfig(YamlConfiguration.loadConfiguration(newFile));

        }

        List<Integer> pageSlots = new ArrayList<>();

        if (this.getMenuConfig().get("page-slots") != null)
            pageSlots = this.getMenuConfig().getIntegerList("page-slots");
        else if (this.getMenuConfig().get("page-range") != null) {

            // dear god
            final List<Integer> pageRange = this.getMenuConfig().getIntegerList("page-range");
            if (pageRange.size() == 2) {

                for (int i = pageRange.get(0); i < pageRange.get(pageRange.size() - 1); i++)
                    pageSlots.add(i);

            }
        } else {
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
        final String materialName = Optional.ofNullable(this.getMenuConfig().getString("filler")).orElse(Material.GRAY_STAINED_GLASS_PANE.name());
        final Material filler = Optional.ofNullable(Material.matchMaterial(materialName)).orElse(Material.GRAY_STAINED_GLASS_PANE);

        gui.setItems(this.getMenuConfig().getIntegerList("filler-slots"), Item.filler(filler).getItem(), event -> {});

        gui.setItem(this.getMenuConfig().getInt("previous-page.slot"), this.getGUIItem("previous-page", null, player), event -> gui.previous(event.getWhoClicked()));
        gui.setItem(this.getMenuConfig().getInt("next-page.slot"), this.getGUIItem("next-page", null, player), event -> gui.next(event.getWhoClicked()));

        final ConfigurationSection section = this.getMenuConfig().getConfigurationSection("extra-items");
        if (section != null) {
            section.getKeys(false).forEach(s -> gui.setItem(section.getInt(s + ".slot"), this.getGUIItem("extra-items." + s, null, player), event -> clickEvent(player, "extra-items." + s, StringPlaceholders.empty())));
        }

        final String emojiUnlocked = this.getMenuConfig().getString("permission-status.unlocked");
        final String emojiLocked = this.getMenuConfig().getString("permission-status-locked");

        for (Emoji emoji : this.getPlugin().getManager(EmojiManager.class).getCachedEmojis()) {

            if (getMenuConfig().getBoolean("display-accessed-only") && !player.hasPermission("chatemojis.emoji.*") && !player.hasPermission("chatemojis.emoji." + emoji.getId()))
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
