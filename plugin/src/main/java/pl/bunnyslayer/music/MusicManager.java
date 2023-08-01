package pl.bunnyslayer.music;

import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MusicManager {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final List<MusicSchema> schemas = new ArrayList<>();
    private final List<MusicPlayer> musicPlayers = new ArrayList<>();

    public void addSchema(MusicSchema schema) {
        if(getByName(schema.getName()) != null) {
            plugin.getLogger().severe("Cannot add music schema: " + schema.getName() + " - found duplicated name!");
            return;
        }
        schemas.add(schema);
    }

    @Nullable
    public MusicSchema getByName(String name) {
        for(MusicSchema schema : schemas) {
            if(schema.getName().equals(name)) {
                return schema;
            }
        }
        return null;
    }

    public void clearSchemas() {
        schemas.clear();
    }

    @Nullable
    public MusicPlayer createPlayer(String musicName, @NotNull Location location) {
        MusicSchema schema = getByName(musicName);
        if(schema == null) {
            plugin.getLogger().severe("Music schema " + musicName + " doesn't exist!");
            return null;
        }
        return new MusicPlayer(schema, location);
    }

    public void removePlayer(@NotNull MusicPlayer musicPlayer) {
        musicPlayer.stop();
        musicPlayers.remove(musicPlayer);
    }

}
