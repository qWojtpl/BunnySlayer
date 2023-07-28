package pl.bunnyslayer.boosters;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class CustomBooster {

    private String name;
    private ItemStack item;

    @NotNull
    public LivingBooster spawnBooster(Location location) {
        Entity entity = location.getWorld().spawnEntity(location, EntityType.DROPPED_ITEM);
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);
        entity.setGravity(false);
        ((Item) entity).setItemStack(item);
        return new LivingBooster((Item) entity);
    }

}
