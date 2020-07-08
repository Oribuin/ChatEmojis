package xyz.oribuin.chatemojis.managers;

import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.guis.MainMenu;
import xyz.oribuin.chatemojis.guis.Menu;

import java.util.LinkedList;

public class GuiManager extends Manager {
    private final LinkedList<Menu> menus = new LinkedList<>();

    public GuiManager(ChatEmojis plugin) {
        super(plugin);
    }

    public void registerMenus() {
        this.menus.add(new MainMenu(plugin, null));
    }

    @Override
    public void reload() {
        menus.forEach(Menu::reload);
    }
}
