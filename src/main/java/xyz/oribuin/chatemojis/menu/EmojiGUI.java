package xyz.oribuin.chatemojis.menu;

import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.PaginatedGui;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.manager.EmojiManager;
import xyz.oribuin.chatemojis.obj.Emoji;
import xyz.oribuin.chatemojis.obj.Menu;

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

        this.getMenuConfig().getIntegerList("filler-slots").forEach(i -> gui.setItem(i, this.fillerItem()));

        gui.setItem(this.getMenuConfig().getInt("previous-page.slot"), ItemBuilder.from(this.getGUIItem("previous-page", null, player))
                .asGuiItem(x -> gui.previous()));

        gui.setItem(this.getMenuConfig().getInt("next-page.slot"), ItemBuilder.from(this.getGUIItem("next-page", null, player))
                .asGuiItem(x -> gui.next()));

        final ConfigurationSection section = this.getMenuConfig().getConfigurationSection("extra-items");
        if (section != null) {
            section.getKeys(false)
                    .forEach(s -> gui.setItem(section.getInt(s + ".slot"), ItemBuilder.from(this.getGUIItem("extra-items." + s, null, player))
                            .asGuiItem()));
        }

        for (Emoji emoji : this.plugin.getManager(EmojiManager.class).getCachedEmojis()) {

            if (!player.hasPermission("chatemojis.emoji.*") && !player.hasPermission("chatemojis.emoji." + emoji.getId())) continue;

            gui.addItem(ItemBuilder.from(this.getGUIItem("emoji", emoji, player)).asGuiItem());
        }

        gui.open(player);
    }

}
