package xyz.oribuin.chatemojis.action.type;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.oribuin.chatemojis.ChatEmojis;
import xyz.oribuin.chatemojis.action.Action;

public class SoundAction implements Action {

    @Override
    public String actionType() {
        return "SOUND";
    }

    @Override
    public void executeAction(ChatEmojis plugin, Player player, String msg) {
        player.playSound(player.getLocation(), Sound.valueOf(msg), 1f, 1f);
    }

}
