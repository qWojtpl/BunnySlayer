package pl.bunnyslayer.music;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MusicPlayer {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final MusicSchema schema;
    private final Location location;
    private final List<Sound> sounds = new ArrayList<>();
    private final List<Float> pitches = new ArrayList<>();
    private final List<Integer> delays = new ArrayList<>();
    private boolean loop;

    public MusicPlayer(@NotNull MusicSchema schema, @NotNull Location location) {
        this.schema = schema;
        this.location = location;
        createData();
    }

    public void start() {
        int currentDelay = 0;
        int i = 0;
        for(Sound sound : sounds) {
            currentDelay += delays.get(i);
            if(sound == null) {
                continue;
            }
            float pitch = pitches.get(i);
            plugin.getServer().getScheduler().runTaskLater(plugin, () ->
                location.getWorld().playSound(location, sound, 6.0F, pitch), currentDelay);
            i++;
        }
        if(loop) {
            plugin.getServer().getScheduler().runTaskLater(plugin, this::start, currentDelay);
        }
    }

    public void stop() {
        loop = false;
    }

    public void createData() {
        for(String data : schema.getMusicData()) {
            String[] split = data.split(" ");
            if(split.length != 3) {
                if(split.length == 2) {
                    if(split[0].equalsIgnoreCase("loop")) {
                        int delay;
                        try {
                            delay = Integer.parseInt(split[1]);
                        } catch(NumberFormatException e) {
                            plugin.getLogger().severe(data + " is not correct loop info!");
                            continue;
                        }
                        sounds.add(null);
                        delays.add(delay);
                        loop = true;
                        continue;
                    }
                }
                plugin.getLogger().severe(data + " is not correct music data!");
                continue;
            }
            Sound sound;
            try {
                sound = Sound.valueOf("BLOCK_NOTE_BLOCK_" + split[0].toUpperCase());
            } catch(IllegalArgumentException e) {
                plugin.getLogger().severe(split[0] + " is not correct sound type!");
                continue;
            }
            float clicks;
            float pitch;
            try {
                clicks = Integer.parseInt(split[1]);
                if(clicks < 0 || clicks > 24) {
                    throw new IllegalArgumentException();
                }
                pitch = (float) Math.pow(2, ((-12 + clicks) / 12));
            } catch(NumberFormatException e) {
                plugin.getLogger().severe(split[1] + " is not correct click count!");
                continue;
            }
            int delay;
            try {
                delay = Integer.parseInt(split[2]);
            } catch(NumberFormatException e) {
                plugin.getLogger().severe(split[2] + " is not correct delay!");
                continue;
            }
            sounds.add(sound);
            pitches.add(pitch);
            delays.add(delay);
        }
    }

}
