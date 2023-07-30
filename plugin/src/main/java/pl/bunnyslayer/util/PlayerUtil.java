package pl.bunnyslayer.util;

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

}
