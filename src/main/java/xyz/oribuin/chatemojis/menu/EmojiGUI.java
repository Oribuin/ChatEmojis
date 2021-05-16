package xyz.oribuin.chatemojis.menu;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.chatemojis.obj.Menu;
import xyz.oribuin.orilibrary.util.HexUtils;
import xyz.oribuin.orilibrary.util.StringPlaceholders;

public class EmojiGUI extends Menu {

    private final ChatEmojis plugin = this.getPlugin();
    private final PaginatedGui gui;

    public EmojiGUI(ChatEmojis plugin) {
        super(plugin, "emoji-menu");
        super.enable();

        this.gui = new PaginatedGui(this.getMenuConfig().getInt("gui-rows"), this.getMenuConfig().getString("gui-name"));
        this.gui.setDefaultClickAction(event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

            ((Player) event.getWhoClicked()).updateInventory();
        });

    }

    /**
     * Create and open the gui for the player
     *
     * @param player The player
     */
    public void createGUI(Player player) {

        this.getMenuConfig().getIntegerList("filler-slots").forEach(i -> gui.setItem(i, this.fillerItem(player)));

        gui.setItem(this.getMenuConfig().getInt("previous-page.slot"), ItemBuilder.from(this.getGUIItem("previous-page", null, player))
                .asGuiItem(x -> gui.previous()));

        gui.setItem(this.getMenuConfig().getInt("next-page.slot"), ItemBuilder.from(this.getGUIItem("next-page", null, player))
                .asGuiItem(x -> gui.next()));

        final ConfigurationSection section = this.getMenuConfig().getConfigurationSection("extra-items");
        if (section != null) {
            section.getKeys(false)
                    .forEach(s -> gui.setItem(section.getInt(s + ".slot"), ItemBuilder.from(this.getGUIItem("extra-items." + s, null, player))
                            .asGuiItem(event -> clickEvent(player, "extra-items." + s, StringPlaceholders.empty()))));
        }

        final String emojiUnlocked = this.getMenuConfig().getString("permission-status.unlocked");
        final String emojiLocked = this.getMenuConfig().getString("permission-status-locked");

        for (Emoji emoji : this.plugin.getManager(EmojiManager.class).getCachedEmojis()) {

            if (getMenuConfig().getBoolean("display-accessed-only") && !player.hasPermission("chatemojis.emoji.*") && !player.hasPermission("chatemojis.emoji." + emoji.getId())) continue;

            StringPlaceholders placeholders = StringPlaceholders.builder("emoji", emoji.getReplacement())
                    .addPlaceholder("check", emoji.getCheck())
                    .addPlaceholder("name", emoji.getName())
                    .addPlaceholder("id", emoji.getId())
                    .addPlaceholder("creator", emoji.getCreator() != null ? Bukkit.getOfflinePlayer(emoji.getCreator()).getName() : this.getMenuConfig().getString("no-creator"))
                    .addPlaceholder("permission", player.hasPermission("chatemojis.emoji." + emoji.getId().toLowerCase()) ?
                            HexUtils.colorify(emojiUnlocked != null ? emojiUnlocked : "Unlocked") : HexUtils.colorify(emojiLocked != null ? emojiLocked : "Locked"))
                    .build();

            gui.addItem(ItemBuilder.from(this.getGUIItem("emoji", emoji, player)).asGuiItem(event -> clickEvent(player, "emoji", placeholders)));
        }

        gui.open(player);
    }


}
