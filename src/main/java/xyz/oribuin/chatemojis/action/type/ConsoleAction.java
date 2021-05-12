package xyz.oribuin.chatemojis.action.type;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.action.Action;

public class ConsoleAction implements Action {
    @Override
    public String actionType() {
        return "CONSOLE";
    }

    @Override
    public void executeAction(ChatEmojis plugin, Player player, String msg) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg);
    }

}
