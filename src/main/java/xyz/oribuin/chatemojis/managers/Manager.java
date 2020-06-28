package xyz.oribuin.chatemojis.managers;

import xyz.oribuin.chatemojis.ChatEmojis;

public abstract class Manager {

    protected final ChatEmojis plugin;

    public Manager(ChatEmojis plugin) {
        this.plugin = plugin;
    }

    public abstract void reload();
}
