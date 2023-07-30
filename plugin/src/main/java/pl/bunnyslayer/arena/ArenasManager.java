package pl.bunnyslayer.arena;

import lombok.Getter;
import org.bukkit.entity.Entity;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.boosters.LivingBooster;
import pl.bunnyslayer.bunnies.LivingBunny;
import pl.bunnyslayer.data.DataHandler;
import pl.bunnyslayer.util.DateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class ArenasManager {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final DataHandler dataHandler = plugin.getDataHandler();
    private final List<Arena> arenas = new ArrayList<>();
    private final HashMap<String, Double> weekPoints = new HashMap<>();

    public ArenasManager() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            String currentHour = DateManager.getHour() + ":" + DateManager.getMinute();
            for(Arena arena : arenas) {
                for(String hour : arena.getStartHours()) {
                    if(hour.equals(currentHour)) {
                        arena.setStarted(true);
                    }
                }
            }
        }, 0L, 20L * 60);
    }

    @Nullable
    public Arena getByName(@Nullable String name) {
        for(Arena arena : arenas) {
            if(arena.getName().equals(name)) {
                return arena;
            }
        }
        return null;
    }

    @Nullable
    public Arena getByBunny(Entity entity) {
        for(Arena arena : arenas) {
            for(LivingBunny bunny : arena.getLivingBunnies()) {
                if(bunny.getEntity().equals(entity)) {
                    return arena;
                }
            }
        }
        return null;
    }

    @Nullable
    public Arena getByBooster(Entity entity) {
        for(Arena arena : arenas) {
            for(LivingBooster booster : arena.getLivingBoosters()) {
                if(booster.getBoosterEntity().equals(entity)) {
                    return arena;
                }
            }
        }
        return null;
    }

    public void addArena(Arena arena) {
        if(getByName(arena.getName()) != null) {
            plugin.getLogger().severe("Cannot add arena: " + arena.getName() + " - found duplicated name!");
            return;
        }
        arenas.add(arena);
    }

    public void addPoints(String player, double points) {
        weekPoints.put(player, weekPoints.getOrDefault(player, 0.0) + points);
        dataHandler.savePoints(player, weekPoints.get(player));
    }

    public void clearArenas() {
        arenas.clear();
    }

    public void clearPoints() {
        for(String player : weekPoints.keySet()) {
            dataHandler.savePoints(player, null);
        }
        weekPoints.clear();
    }

}
