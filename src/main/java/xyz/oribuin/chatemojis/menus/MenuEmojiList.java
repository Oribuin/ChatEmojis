package xyz.oribuin.chatemojis.menus;

import dev.esophose.guiframework.GuiFactory;
import dev.esophose.guiframework.GuiFramework;
import dev.esophose.guiframework.gui.*;
import dev.esophose.guiframework.gui.screen.GuiScreen;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.hooks.PAPI;

import java.io.File;
import java.util.*;

public class MenuEmojiList {
    private ChatEmojis chatEmojis = ChatEmojis.getInstance();
    private GuiFramework guiFramework;
    private GuiContainer guiContainer;
    private Player player;


    public MenuEmojiList(Player player) {
        this.guiFramework = GuiFramework.instantiate(chatEmojis);
        this.guiContainer = null;
        this.player = player;
    }

    public void openFor() {
        if (this.isInvalid())
            this.buildGui();
        this.guiContainer.openFor(player);
    }

    public void buildGui() {
        this.guiContainer = dev.esophose.guiframework.GuiFactory.createContainer();

        FileConfiguration menuConfig = chatEmojis.getConfig();
        FileConfiguration emojiConfig = YamlConfiguration.loadConfiguration(new File(chatEmojis.getDataFolder(), "emojis.yml"));
        ConfigurationSection emojiSec = emojiConfig.getConfigurationSection("emojis");
        if (emojiSec == null) return;

        List<Integer> borderSlots = new ArrayList<>();
        for (int i = 0; i <= 8; i++) borderSlots.add(i);
        for (int i = 9; i <= 36; i += 9) borderSlots.add(i);
        for (int i = 17; i <= 44; i += 9) borderSlots.add(i);
        for (int i = 45; i <= 53; i++) borderSlots.add(i);
        borderSlots.addAll(Arrays.asList(45, 53));

        List<Integer> emojiSlots = new ArrayList<>();
        for (int i = 10; i <= 16; i++) emojiSlots.add(i);
        for (int i = 19; i <= 25; i++) emojiSlots.add(i);
        for (int i = 28; i <= 34; i++) emojiSlots.add(i);
        for (int i = 37; i <= 43; i++) emojiSlots.add(i);

        ItemStack borderItem = new ItemStack(Material.valueOf(menuConfig.getString("menu.border")));
        ItemMeta itemMeta = borderItem.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(" ");
            itemMeta.addItemFlags(ItemFlag.values());
            borderItem.setItemMeta(itemMeta);
        }

        List<String> emojiList = new ArrayList<>();
        emojiSec.getKeys(false).stream().filter(emoji -> player.hasPermission(emoji + ".permission")).forEach(emojiList::add);
        int emojiCount = emojiList.size();

        GuiScreen mainScreen = GuiFactory.createScreen(this.guiContainer, GuiSize.ROWS_SIX)
                .setTitle(PAPI.apply(player, menuConfig.getString("menu.title").replace("%emojis%", "" + emojiCount)));

        for (int slot : borderSlots)
            mainScreen.addItemStackAt(slot, borderItem);


        mainScreen.addButtonAt(menuConfig.getInt("menu.back-page.slot"), GuiFactory.createButton()
                .setIcon(Material.valueOf(menuConfig.getString("menu.back-page.material")))
                .setName(Objects.requireNonNull(menuConfig.getString("menu.back-page.name")))
                .setClickAction(event -> {
                    if (menuConfig.getBoolean("menu.use-sound", true)) {
                        Player cplayer = (Player) event.getWhoClicked();
                        cplayer.playSound(cplayer.getLocation(), Sound.valueOf(menuConfig.getString("menu.click-sound")), 100, 1);
                    }

                    return ClickAction.PAGE_BACKWARDS;
                })
                .setFlags(GuiButtonFlag.HIDE_IF_FIRST_PAGE)
                .setLore(menuConfig.getStringList("menu.back-page.lore"))
                .setGlowing(menuConfig.getBoolean("menu.back-page.glowing"))
                .setHiddenReplacement(borderItem))

                .addButtonAt(menuConfig.getInt("menu.close-menu.slot"), GuiFactory.createButton()
                        .setIcon(Material.valueOf(menuConfig.getString("menu.close-menu.material")))
                        .setName(menuConfig.getString("menu.close-menu.name"))
                        .setGlowing(menuConfig.getBoolean("menu.close-menu.glowing"))
                        .setClickAction(event -> {
                            if (menuConfig.getBoolean("menu.use-sound", true)) {
                                Player cplayer = (Player) event.getWhoClicked();
                                cplayer.playSound(cplayer.getLocation(), Sound.valueOf(menuConfig.getString("menu.click-sound")), 100, 1);
                            }

                            return ClickAction.CLOSE;
                        }))

                .addButtonAt(menuConfig.getInt("menu.forward-page.slot"), GuiFactory.createButton()
                        .setIcon(Material.valueOf(menuConfig.getString("menu.forward-page.material")))
                        .setName(menuConfig.getString("menu.forward-page.name"))
                        .setClickAction(event -> {
                            if (menuConfig.getBoolean("menu.use-sound", true)) {
                                Player cplayer = (Player) event.getWhoClicked();
                                cplayer.playSound(cplayer.getLocation(), Sound.valueOf(menuConfig.getString("menu.click-sound")), 100, 1);
                            }

                            return ClickAction.PAGE_FORWARDS;
                        })
                        .setFlags(GuiButtonFlag.HIDE_IF_LAST_PAGE)
                        .setLore(menuConfig.getStringList("menu.forward-page.lore"))
                        .setGlowing(menuConfig.getBoolean("menu.forward-page.glowing"))
                        .setHiddenReplacement(borderItem));

        Iterator<Integer> slotIterator = emojiSlots.iterator();

        for (String emoji : emojiSec.getKeys(false)) {

            String name = emojiSec.getString(emoji + ".name");
            String permission = emojiSec.getString(emoji + ".permission");
            String check = emojiSec.getString(emoji + ".check");
            String replacement = emojiSec.getString(emoji + ".replacement");

            if (name == null || permission == null || check == null || replacement == null) return;

            List<String> arrayList = new ArrayList<>();

            for (String line : menuConfig.getStringList("menu.emoji.lore")) {
                arrayList.add(line
                        .replace("%emoji_check%", check)
                        .replace("%emoji_replacement%", replacement)
                        .replace("%emoji_permission%", permission));
            }

            if (player.hasPermission(permission)) {
                mainScreen.addButtonAt(slotIterator.next(), GuiFactory.createButton()
                        .setName(menuConfig.getString("menu.emoji.name").replace("%emoji_name%", name))
                        .setLore(arrayList)
                        .setClickAction(event -> {
                            if (menuConfig.getBoolean("menu.use-sound", true)) {
                                Player cplayer = (Player) event.getWhoClicked();
                                cplayer.playSound(cplayer.getLocation(), Sound.valueOf(menuConfig.getString("menu.click-sound")), 100, 1);
                            }

                            return ClickAction.REFRESH;
                        })
                        .setIcon(Material.valueOf(menuConfig.getString("menu.emoji.material")))
                        .setGlowing(menuConfig.getBoolean("menu.emoji.glowing")));
            }
        }

        guiContainer.addScreen(mainScreen);
        this.guiFramework.getGuiManager().registerGui(this.guiContainer);
    }


    public boolean isInvalid() {
        return this.guiContainer == null || !this.guiFramework.getGuiManager().getActiveGuis().contains(this.guiContainer);
    }
}
