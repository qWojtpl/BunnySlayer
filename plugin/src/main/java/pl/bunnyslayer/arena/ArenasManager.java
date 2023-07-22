package pl.bunnyslayer.arena;

import lombok.Getter;
import pl.bunnyslayer.BunnySlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ArenasManager {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final List<Arena> arenas = new ArrayList<>();

    @Nullable
    public Arena getByName(@Nullable String name) {
        for(Arena arena : arenas) {
            if(arena.getName().equals(name)) {
                return arena;
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

    public void clearArenas() {
        arenas.clear();
    }

}
