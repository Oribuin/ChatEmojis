package xyz.oribuin.chatemojis.action.type;

import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.action.Action;

public class CloseAction implements Action {
    @Override
    public String actionType() {
        return "CLOSE";
    }

    @Override
    public void executeAction(ChatEmojis plugin, Player player, String msg) {
        player.closeInventory();
    }

}
