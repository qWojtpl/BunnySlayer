package pl.bunnyslayer.bunnies;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class CustomBunny {

    private BunnyType bunnyType;
    private double speed;
    private double percentage;
    private double knockBack;
    private double damage;
    private double experience;

    @NotNull
    public LivingBunny spawnBunny(Location location) {
        LivingEntity e = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.RABBIT);
        e.setCustomNameVisible(true);
        e.setMaxHealth(1);
        e.setHealth(1);
        LivingBunny bunny = new LivingBunny(e);
        bunny.setExperience(experience);
        bunny.setKnockBack(knockBack);
        bunny.createName();
        return bunny;
    }

}
