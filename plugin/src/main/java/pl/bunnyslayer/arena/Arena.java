package pl.bunnyslayer.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Arena {

    private final String name;
    private int duration;
    private List<String> startHours = new ArrayList<>();
    private List<Location> spawnLocations = new ArrayList<>();
    private final List<CustomBunny> customBunnies = new ArrayList<>();

    public Arena(String name) {
        this.name = name;
    }

    public void addCustomBunny(CustomBunny customBunny) {
        customBunnies.add(customBunny);
    }

}
