package pl.bunnyslayer.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;

import javax.annotation.Nullable;

public class LocationUtil {

    @Nullable
    public static Location parseLocation(@NotNull String locationString) {
        String[] split = locationString.split(" ");
        if(split.length != 4) {
            BunnySlayer.getInstance().getLogger().severe("Location " + locationString + " is not correct!");
            return null;
        }
        World w = BunnySlayer.getInstance().getServer().getWorld(split[3]);
        if(w == null) {
            BunnySlayer.getInstance().getLogger().severe("Location " + locationString + " is not correct, " +
                    "can't found world " + split[3]);
            return null;
        }
        double x, y, z;
        try {
            x = Double.parseDouble(split[0]);
            y = Double.parseDouble(split[1]);
            z = Double.parseDouble(split[2]);
        } catch(NumberFormatException e) {
            BunnySlayer.getInstance().getLogger().severe("Location " + locationString + " is not correct!");
            return null;
        }
        return new Location(w, x, y, z);
    }

}
