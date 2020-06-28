package xyz.oribuin.chatemojis.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Emoji {

    private static Emoji instance;

    private final Player creator;
    private final String name;
    private final String check;
    private final String replacement;
    private final List<Emoji> emojiList;

    public Emoji(Player creator, String name, String check, String replacement) {
        instance = this;

        this.creator = creator;
        this.name = name;
        this.check = check;
        this.replacement = replacement;
        this.emojiList = new ArrayList<>();
    }

    public Player getCreator() {
        return creator;
    }

    public String getCheck() {
        return check;
    }

    public String getName() {
        return name;
    }

    public String getReplacement() {
        return replacement;
    }

    public List<Emoji> getEmojiList() {
        return emojiList;
    }

    public static Emoji getInstance() {
        return instance;
    }
}
