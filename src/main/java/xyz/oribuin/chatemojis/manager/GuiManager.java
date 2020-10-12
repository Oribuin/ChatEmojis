package xyz.oribuin.chatemojis.manager;

import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.menu.MainMenu;
import xyz.oribuin.chatemojis.menu.Menu;
import xyz.oribuin.chatemojis.menu.MyEmojis;

import java.util.LinkedList;

public class GuiManager extends Manager {
    private final LinkedList<Menu> menus = new LinkedList<>();

    public GuiManager(ChatEmojis plugin) {
        super(plugin);
    }

    public void registerMenus() {
        this.menus.add(new MainMenu(plugin, null));
        this.menus.add(new MyEmojis(plugin, null));
    }

    @Override
    public void reload() {
        menus.forEach(Menu::reload);
    }
}
