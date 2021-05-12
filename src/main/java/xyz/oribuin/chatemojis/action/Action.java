package xyz.oribuin.chatemojis.action;

import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;

public interface Action {

    String actionType();

    void executeAction(ChatEmojis plugin, Player player, String msg);

}
