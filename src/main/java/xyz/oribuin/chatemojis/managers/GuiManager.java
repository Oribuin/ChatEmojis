package xyz.oribuin.chatemojis.managers;

import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.guis.MainMenu;
import xyz.oribuin.chatemojis.guis.Menu;
import xyz.oribuin.chatemojis.guis.MyEmojis;

import java.util.Arrays;
import java.util.LinkedList;

public class GuiManager extends Manager {
    private final LinkedList<Menu> menus = new LinkedList<>();

    public GuiManager(ChatEmojis plugin) {
        super(plugin);
    }

    public void registerMenus() {
        this.menus.addAll(Arrays.asList(
                new MainMenu(plugin, null),
                new MyEmojis(plugin, null))
        );
    }

    @Override
    public void reload() {
        menus.forEach(Menu::reload);
    }
}
