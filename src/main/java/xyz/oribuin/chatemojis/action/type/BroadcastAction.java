package xyz.oribuin.chatemojis.action.type;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.action.Action;
import xyz.oribuin.orilibrary.util.HexUtils;

public class BroadcastAction implements Action {

    @Override
    public String actionType() {
        return "BROADCAST";
    }

    @Override
    public void executeAction(ChatEmojis plugin, Player player, String msg) {
        Bukkit.broadcast(HexUtils.colorify(msg), "");
    }

}
