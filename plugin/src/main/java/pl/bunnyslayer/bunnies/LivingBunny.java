package pl.bunnyslayer.bunnies;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;

@Getter
@Setter
public class LivingBunny {

    private final LivingEntity entity;
    private BunnyType bunnyType = BunnyType.NORMAL;
    private double experience;
    private double knockBack;

    public LivingBunny(LivingEntity entity) {
        this.entity = entity;
    }

    public void setExperience(double experience) {
        this.experience = experience;
        createName();
    }

    public void createName() {
        String color = "§a+ ";
        if(experience < 0) {
            color = "§4- ";
        }
        entity.setCustomName(color + experience);
        if(bunnyType.equals(BunnyType.KILLER)) {
            ((Rabbit) entity).setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
        }
    }

}
