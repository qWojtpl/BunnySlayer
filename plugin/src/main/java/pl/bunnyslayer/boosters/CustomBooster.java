package pl.bunnyslayer.boosters;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.arena.Arena;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomBooster {

    private String name;
    private double percentage;
    private ItemStack item;
    private List<String> effects = new ArrayList<>();

    @NotNull
    public LivingBooster spawnBooster(@NotNull Location location, @NotNull Arena arena) {
        Entity entity = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        entity.setCustomName(name.replace("&", "§"));
        entity.setCustomNameVisible(true);
        entity.setInvulnerable(true);
        ArmorStand as = (ArmorStand) entity;
        as.setInvisible(true);
        as.setGravity(false);
        as.setItem(EquipmentSlot.HEAD, item);
        ArmorStand.LockType lockType = ArmorStand.LockType.ADDING_OR_CHANGING;
        as.addEquipmentLock(EquipmentSlot.HEAD, lockType);
        as.addEquipmentLock(EquipmentSlot.CHEST, lockType);
        as.addEquipmentLock(EquipmentSlot.LEGS, lockType);
        as.addEquipmentLock(EquipmentSlot.FEET, lockType);
        as.addEquipmentLock(EquipmentSlot.HAND, lockType);
        as.addEquipmentLock(EquipmentSlot.OFF_HAND, lockType);
        LivingBooster booster = new LivingBooster(as);
        booster.setEffects(effects);
        return booster;
    }

}
