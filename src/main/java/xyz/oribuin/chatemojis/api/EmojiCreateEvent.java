package xyz.oribuin.chatemojis.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EmojiCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private final String emojiName;
    private final String emojiCheck;
    private final String emojiReplacement;
    private final Player creator;
    private boolean isCancelled = false;

    public EmojiCreateEvent(String emojiName, String emojiCheck, String emojiReplacement, Player creator) {
        this.emojiName = emojiName;
        this.emojiCheck = emojiCheck;
        this.emojiReplacement = emojiReplacement;
        this.creator = creator;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getCreator() {
        return creator;
    }

    public String getEmojiCheck() {
        return emojiCheck;
    }

    public String getEmojiReplacement() {
        return emojiReplacement;
    }

    public String getEmojiName() {
        return emojiName;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public String getEventName() {
        return super.getEventName();
    }
}
