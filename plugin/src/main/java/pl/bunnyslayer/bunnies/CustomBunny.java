package pl.bunnyslayer.bunnies;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class CustomBunny {

    private BunnyType bunnyType;
    private int speed;
    private double percentage;
    private double launchForce;
    private double experience;

    @NotNull
    public LivingBunny spawnBunny(@NotNull Location location) {
        LivingEntity e = (LivingEntity) location.getWorld().spawnEntity(location, EntityType.RABBIT);
        e.setCustomNameVisible(true);
        e.setMaxHealth(1);
        e.setHealth(1);
        e.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speed, true, false));
        if(bunnyType.equals(BunnyType.KILLER)) {
            ((Rabbit) e).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
        }
        location.getWorld().spawnParticle(Particle.GLOW_SQUID_INK, location, 3);
        LivingBunny bunny = new LivingBunny(e);
        bunny.setExperience(experience);
        bunny.setLaunchForce(launchForce);
        bunny.createName();
        return bunny;
    }

}
