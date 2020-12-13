package xyz.oribuin.chatemojis.manager;

import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.menu.MainMenu;
import xyz.oribuin.chatemojis.menu.Menu;
import xyz.oribuin.chatemojis.menu.MyEmojis;
import xyz.oribuin.orilibrary.Manager;

import java.util.LinkedList;

public class GuiManager extends Manager {
    private final LinkedList<Menu> menus = new LinkedList<>();

    public GuiManager(ChatEmojis plugin) {
        super(plugin);
    }

    public void registerMenus() {
        this.menus.add(new MainMenu((ChatEmojis) getPlugin(), null));
        this.menus.add(new MyEmojis((ChatEmojis) getPlugin(), null));
    }

    @Override
    public void enable() {
        this.registerMenus();
        this.menus.forEach(Menu::enable);
    }

    @Override
    public void disable() {
        this.menus.clear();
    }
}
