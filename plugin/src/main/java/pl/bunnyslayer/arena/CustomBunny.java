package pl.bunnyslayer.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

@Getter
@Setter
public class CustomBunny {

    private BunnyType bunnyType;
    private double speed;
    private double percentage;
    private double knockBack;
    private double damage;
    private double experience;

    public void spawnBunny(Location location) {
        Entity e = location.getWorld().spawnEntity(location, EntityType.RABBIT);
        e.setCustomNameVisible(true);
        String color = "§a+ ";
        if(experience < 0) {
            color = "§4- ";
        }
        e.setCustomName(color + experience);
    }

}
