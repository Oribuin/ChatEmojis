package xyz.oribuin.chatemojis.action.type;

import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.action.Action;
import xyz.oribuin.orilibrary.util.HexUtils;

public class MessageAction implements Action {
    @Override
    public String actionType() {
        return "MESSAGE";
    }

    @Override
    public void executeAction(ChatEmojis plugin, Player player, String msg) {
        player.sendMessage(HexUtils.colorify(msg));
    }
}
