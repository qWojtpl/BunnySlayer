package pl.bunnyslayer.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.boosters.CustomBooster;
import pl.bunnyslayer.boosters.LivingBooster;
import pl.bunnyslayer.bunnies.CustomBunny;
import pl.bunnyslayer.bunnies.LivingBunny;
import pl.bunnyslayer.util.RandomNumber;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Arena {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final String name;
    private double duration;
    private double boosterInterval;
    private int arenaTask = -1;
    private int boosterTask = -1;
    private boolean started;
    private List<String> startHours = new ArrayList<>();
    private List<Location> spawnLocations = new ArrayList<>();
    private List<Location> boosterSpawnLocations = new ArrayList<>();
    private final List<CustomBunny> customBunnies = new ArrayList<>();
    private final List<LivingBunny> livingBunnies = new ArrayList<>();
    private final List<CustomBooster> customBoosters = new ArrayList<>();
    private final List<LivingBooster> livingBoosters = new ArrayList<>();

    public Arena(String name) {
        this.name = name;
    }

    public void addCustomBunny(CustomBunny customBunny) {
        customBunnies.add(customBunny);
    }

    public void addCustomBooster(CustomBooster customBooster) {
        customBoosters.add(customBooster);
    }

    public void setStarted(boolean state) {
        if(!this.started) {
            if(state) {
                startArena();
            }
        } else {
            if(!state) {
                stopArena();
            }
        }
    }

    public void startArena() {
        if(this.started) {
            return;
        }
        this.started = true;
        arenaTask = plugin.getServer().getScheduler().runTaskLater(plugin, this::stopArena, 20L * 60 * (long) duration).getTaskId();
        for(int i = 0; i < 50; i++) {
            spawnBunny(getRandomCustomBunny());
        }
    }

    public void spawnBunny(CustomBunny bunny) {
        Location spawnLocation = getRandomLocation();
        if(spawnLocation == null) {
            return;
        }
        livingBunnies.add(bunny.spawnBunny(spawnLocation));
    }

    public void spawnBooster(CustomBooster booster) {
        Location spawnLocation = getRandomBoosterLocation();
        if(spawnLocation == null) {
            return;
        }
        livingBoosters.add(booster.spawnBooster(spawnLocation));
    }

    public void stopArena() {
        if(!this.started) {
            return;
        }
        this.started = false;
        plugin.getServer().getScheduler().cancelTask(arenaTask);
        plugin.getServer().getScheduler().cancelTask(boosterTask);
        killAllBunnies();
    }

    @Nullable
    public Location getRandomLocation() {
        if(spawnLocations.size() == 0) {
            return null;
        }
        return spawnLocations.get(RandomNumber.randomInt(0, spawnLocations.size() - 1));
    }

    @Nullable
    public Location getRandomBoosterLocation() {
        if(boosterSpawnLocations.size() == 0) {
            return null;
        }
        return boosterSpawnLocations.get(RandomNumber.randomInt(0, boosterSpawnLocations.size() - 1));
    }

    @Nullable
    public CustomBunny getRandomCustomBunny() {
        int random = RandomNumber.randomInt(1, 100);
        double lastPercentage = 0;
        for(CustomBunny customBunny : customBunnies) {
            if(random > lastPercentage && random <= customBunny.getPercentage() + lastPercentage) {
                return customBunny;
            }
            lastPercentage += customBunny.getPercentage();
        }
        return null;
    }

    @Nullable
    public LivingBunny getLivingBunny(LivingEntity entity) {
        for(LivingBunny livingBunny : livingBunnies) {
            if(livingBunny.getEntity().equals(entity)) {
                return livingBunny;
            }
        }
        return null;
    }

    @Nullable
    public LivingBooster getLivingBooster(Item item) {
        for(LivingBooster livingBooster : livingBoosters) {
            if(livingBooster.getBoosterItem().equals(item)) {
                return livingBooster;
            }
        }
        return null;
    }

    public void killAllBunnies() {
        for(LivingBunny bunny : livingBunnies) {
            bunny.getEntity().remove();
        }
        livingBunnies.clear();
    }

}
