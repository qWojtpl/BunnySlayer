package pl.bunnyslayer.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import pl.bunnyslayer.BunnySlayer;

import javax.annotation.Nullable;

public class PlayerUtil {

    @Nullable
    public static Player getPlayer(String nickname) {
        for(Player p : BunnySlayer.getInstance().getServer().getOnlinePlayers()) {
            if(p.getName().equals(nickname)) {
                return p;
            }
        }
        return null;
    }

    public static void sendActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

}
