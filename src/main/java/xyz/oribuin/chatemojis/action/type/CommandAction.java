package xyz.oribuin.chatemojis.action.type;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.action.Action;

public class CommandAction implements Action {

    @Override
    public String actionType() {
        return "PLAYER";
    }

    @Override
    public void executeAction(ChatEmojis plugin, Player player, String msg) {
        Bukkit.dispatchCommand(player, msg);
    }

}
