package pl.bunnyslayer.boosters;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;

import java.util.List;

@Getter
@Setter
public class LivingBooster {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final ArmorStand boosterEntity;
    private final int aliveTask;
    private List<String> effects;
    private Arena arena;

    public LivingBooster(ArmorStand boosterEntity) {
        this.boosterEntity = boosterEntity;
        aliveTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            boosterEntity.setHeadPose(boosterEntity.getHeadPose().add(0.1, 0.1, 0.1));
            List<Entity> entities = boosterEntity.getNearbyEntities(1, 1, 1);
            for(Entity e : entities) {
                if(!(e instanceof Player)) {
                    continue;
                }
                applyEffects((Player) e);
                remove();
                return;
            }
        }, 0L, 1L);
    }

    public void applyEffects(Player player) {
        for(String effect : effects) {
            int amplifier;
            int duration;
            try {
                String[] split = effect.split(" ");
                if(split.length != 3) {
                    throw new IllegalArgumentException();
                }
                PotionEffectType potionEffectType = PotionEffectType.getByName(split[0]);
                if(potionEffectType == null) {
                    throw new IllegalArgumentException();
                }
                amplifier = Integer.parseInt(split[1]);
                duration = Integer.parseInt(split[2]) * 20;
                player.addPotionEffect(new PotionEffect(potionEffectType, duration, amplifier));
            } catch(NumberFormatException e) {
                plugin.getLogger().severe(effect + " is not correct effect! Correct format is EFFECT <AMPLIFIER> <DURATION>");
            }
        }
    }

    public void remove() {
        plugin.getServer().getScheduler().cancelTask(aliveTask);
        boosterEntity.remove();
        if(arena != null) {
            arena.getLivingBoosters().remove(this);
        }
    }

}
