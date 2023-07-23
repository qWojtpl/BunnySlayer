package pl.bunnyslayer.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import pl.bunnyslayer.BunnySlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Arena {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final String name;
    private int duration;
    private int arenaTask = -1;
    private boolean started;
    private List<String> startHours = new ArrayList<>();
    private List<Location> spawnLocations = new ArrayList<>();
    private final List<CustomBunny> customBunnies = new ArrayList<>();

    public Arena(String name) {
        this.name = name;
    }

    public void addCustomBunny(CustomBunny customBunny) {
        customBunnies.add(customBunny);
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
        arenaTask = plugin.getServer().getScheduler().runTaskLater(plugin, this::stopArena, 20L * duration).getTaskId();
        for(CustomBunny bunny : customBunnies) {
            bunny.spawnBunny(getRandomLocation());
        }
    }

    public void stopArena() {
        if(!this.started) {
            return;
        }
        this.started = false;
        plugin.getServer().getScheduler().cancelTask(arenaTask);
    }

    public Location getRandomLocation() {
        if(spawnLocations.size() == 0) {
            return null;
        }
        return spawnLocations.get(new Random().nextInt(spawnLocations.size() - 1));
    }

}
