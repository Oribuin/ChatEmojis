package xyz.oribuin.chatemojis.guis;

import dev.rosewood.guiframework.GuiFactory;
import dev.rosewood.guiframework.GuiFramework;
import dev.rosewood.guiframework.gui.ClickAction;
import dev.rosewood.guiframework.gui.GuiButton;
import dev.rosewood.guiframework.gui.GuiContainer;
import dev.rosewood.guiframework.gui.GuiSize;
import dev.rosewood.guiframework.gui.screen.GuiPageContentsResult;
import dev.rosewood.guiframework.gui.screen.GuiScreen;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.hooks.PlaceholderAPIHook;
import xyz.oribuin.chatemojis.managers.EmojiManager;
import xyz.oribuin.chatemojis.utils.HexUtils;
import xyz.oribuin.chatemojis.utils.StringPlaceholders;

import java.util.*;

public class MyEmojis extends Menu {

    private final ChatEmojis plugin;
    private final GuiFramework guiFramework;
    private final Player player;
    private GuiContainer guiContainer;

    public MyEmojis(ChatEmojis plugin, Player player) {
        super(plugin, "my-emojis");
        this.plugin = plugin;
        this.guiFramework = GuiFramework.instantiate(this.plugin);
        this.player = player;
        this.guiContainer = null;

    }

    public void openGui() {
        if (this.isInvalid())
            this.buildGui();
        this.guiContainer.openFor(player);
    }

    private GuiScreen mainScreen() {
        EmojiManager emojiManager = this.plugin.getEmojiManager();

        GuiScreen guiScreen = GuiFactory.createScreen(this.guiContainer, GuiSize.ROWS_SIX)
                .setTitle(HexUtils.colorify(this.getValue("menu-name")));

        // Define configuration files
        ConfigurationSection config = emojiManager.getEmojiSec();
        if (config == null)
            return null;

        this.borderSlots().forEach(integer -> guiScreen.addItemStackAt(integer, this.getItem("border-item")));

        // Create my-emojis item
        guiScreen.addItemStackAt(getMenuConfig().getInt("my-emojis.slot"), getItem("my-emojis"));

        // Create Back page item
        if (this.getMenuConfig().getString("back-page") != null) {
            List<String> lore = new ArrayList<>();
            for (String string : this.getMenuConfig().getStringList("back-page.lore"))
                lore.add(this.format(string, StringPlaceholders.empty()));

            guiScreen.addButtonAt(getMenuConfig().getInt("back-page.slot"), GuiFactory.createButton()
                    .setName(this.format(getMenuConfig().getString("back-page.name"), StringPlaceholders.empty()))
                    .setLore(lore)
                    .setIcon(Material.valueOf(getMenuConfig().getString("back-page.material")))
                    .setGlowing(getMenuConfig().getBoolean("back-page.glowing"))
                    .setHiddenReplacement(this.getItem("border-item"))
                    .setClickAction(event -> {
                        if (getMenuConfig().getBoolean("use-sound")) {
                            player.playSound(player.getLocation(), Sound.valueOf(getMenuConfig().getString("click-sound")), 100, 1);
                        }
                        return ClickAction.PAGE_BACKWARDS;
                    })
            );
        }

        // Create forward page item
        if (this.getMenuConfig().getString("forward-page") != null) {
            List<String> lore = new ArrayList<>();
            for (String string : this.getMenuConfig().getStringList("forward-page.lore"))
                lore.add(this.format(string, StringPlaceholders.empty()));

            guiScreen.addButtonAt(getMenuConfig().getInt("forward-page.slot"), GuiFactory.createButton()
                    .setName(this.format(getMenuConfig().getString("forward-page.name"), StringPlaceholders.empty()))
                    .setLore(lore)
                    .setIcon(Material.valueOf(getMenuConfig().getString("forward-page.material")))
                    .setGlowing(getMenuConfig().getBoolean("forward-page.glowing"))
                    .setHiddenReplacement(this.getItem("border-item"))
                    .setClickAction(event -> {
                        if (getMenuConfig().getBoolean("forward-sound")) {
                            player.playSound(player.getLocation(), Sound.valueOf(getMenuConfig().getString("click-sound")), 100, 1);
                        }
                        return ClickAction.PAGE_FORWARDS;
                    })
            );
        }

        List<String> emojis = new ArrayList<>(config.getKeys(false));

        guiScreen.setPaginatedSection(GuiFactory.createScreenSection(this.emojiSlots()), emojis.size(), (pageNumber, startIndex, endIndex) -> {
            GuiPageContentsResult results = GuiFactory.createPageContentsResult();
            for (int i = startIndex; i < Math.min(endIndex, emojis.size()); i++) {
                String emoji = emojis.get(i);

                String emojiName = config.getString(emoji + ".gui-name");
                String emojiCheck = config.getString(emoji + ".check");
                String emojiReplacement = config.getString(emoji + ".replacement");
                String emojiCreator;

                if (config.getString(emoji + ".creator") == null) {
                    emojiCreator = getMenuConfig().getString("emoji-creator");
                } else {
                    Player creator = Bukkit.getPlayer(UUID.fromString(Objects.requireNonNull(config.getString(emoji + ".creator"))));
                    if (creator == null) {
                        emojiCreator = getMenuConfig().getString("emoji-creator");
                    } else {
                        emojiCreator = creator.getName();
                    }
                }

                StringPlaceholders placeholders = StringPlaceholders.builder()
                        .addPlaceholder("emoji_name", emoji)
                        .addPlaceholder("emoji_creator", emojiCreator)
                        .addPlaceholder("emoji_check", emojiCheck)
                        .addPlaceholder("emoji_replacement", emojiReplacement)
                        .build();


                List<String> lore = new ArrayList<>();
                for (String string : this.getMenuConfig().getStringList("emoji-item.lore"))
                    lore.add(HexUtils.colorify(this.format(string, placeholders)));

                GuiButton guiButton = GuiFactory.createButton()
                        .setName(HexUtils.colorify(this.format(emojiName, placeholders)))
                        .setLore(lore)
                        .setIcon(Material.valueOf(getMenuConfig().getString("emoji-item.material")))
                        .setGlowing(getMenuConfig().getBoolean("emoji-item.glowing"))
                        .setClickAction(event -> {
                            if (getMenuConfig().getBoolean("use-sound")) {
                                player.playSound(player.getLocation(), Sound.valueOf(getMenuConfig().getString("click-sound")), 100, 1);
                            }

                            StringPlaceholders plho = StringPlaceholders.builder()
                                    .addPlaceholder("emoji_name", emoji)
                                    .addPlaceholder("emoji_creator", emojiCreator)
                                    .addPlaceholder("emoji_check", emojiCheck)
                                    .addPlaceholder("emoji_replacement", emojiReplacement)
                                    .addPlaceholder("player", event.getWhoClicked().getName())
                                    .build();


                            if (!getMenuConfig().getStringList("emoji-item.player-commands").isEmpty()) {
                                getMenuConfig().getStringList("emoji-item.player-commands").forEach(s -> player.performCommand(this.format(s, plho)));
                            }

                            if (!getMenuConfig().getStringList("emoji-item.console-commands").isEmpty()) {
                                getMenuConfig().getStringList("emoji-item.console-commands").forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.format(s, plho)));
                            }
                            return ClickAction.CLOSE;
                        });

                if (player.getUniqueId().equals(UUID.fromString(emojiCreator))) {
                    results.addPageContent(guiButton);
                }
            }

            return results;
        });

        return guiScreen;
    }

    private ItemStack getItem(String name) {
        ItemStack itemStack = new ItemStack(Material.valueOf(this.getMenuConfig().getString(name + ".material")));
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return new ItemStack(Material.AIR);

        itemMeta.setDisplayName(this.getValue(name + ".name"));

        List<String> lore = new ArrayList<>();
        for (String string : this.getMenuConfig().getStringList(name + ".lore"))
            lore.add(this.format(string, StringPlaceholders.empty()));
        itemMeta.setLore(lore);

        if (this.getMenuConfig().getBoolean(name + ".glowing")) {
            itemMeta.addEnchant(Enchantment.MENDING, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        for (String itemFlag : this.getMenuConfig().getStringList(name + ".item-flags")) {
            itemMeta.addItemFlags(ItemFlag.valueOf(itemFlag));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public String getValue(String configValue) {
        if (this.getMenuConfig().getString(configValue) == null)
            // Today's lesson of what not to do in Java: DONT PUT "NULL" LIKE I DID, THAT IS WRONG
            return null;

        return HexUtils.colorify(PlaceholderAPIHook.apply(player, this.getMenuConfig().getString(configValue)));
    }

    public String format(String msg, StringPlaceholders placeholders) {
        return HexUtils.colorify(placeholders.apply(PlaceholderAPIHook.apply(player, msg)));
    }

    private void buildGui() {

        this.guiContainer = GuiFactory.createContainer();

        this.guiContainer.addScreen(this.mainScreen());
        this.guiFramework.getGuiManager().registerGui(guiContainer);
    }

    private boolean isInvalid() {
        return this.guiContainer == null || !this.guiFramework.getGuiManager().getActiveGuis().contains(this.guiContainer);
    }

    private List<Integer> borderSlots() {
        java.util.List<Integer> slots = new ArrayList<>();

        for (int i = 0; i <= 8; i++) slots.add(i);
        for (int i = 9; i <= 36; i += 9) slots.add(i);
        for (int i = 17; i <= 44; i += 9) slots.add(i);
        for (int i = 45; i <= 53; i++) slots.add(i);
        slots.addAll(Arrays.asList(45, 53));

        return slots;
    }

    private List<Integer> emojiSlots() {

        List<Integer> emojiSlots = new ArrayList<>();
        for (int i = 10; i <= 16; i++) emojiSlots.add(i);
        for (int i = 19; i <= 25; i++) emojiSlots.add(i);
        for (int i = 28; i <= 34; i++) emojiSlots.add(i);
        for (int i = 37; i <= 43; i++) emojiSlots.add(i);

        return emojiSlots;
    }
}

