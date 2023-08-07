package pl.bunnyslayer.arena;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.boosters.CustomBooster;
import pl.bunnyslayer.boosters.LivingBooster;
import pl.bunnyslayer.bunnies.CustomBunny;
import pl.bunnyslayer.bunnies.LivingBunny;
import pl.bunnyslayer.data.MessagesManager;
import pl.bunnyslayer.music.MusicPlayer;
import pl.bunnyslayer.util.LocationUtil;
import pl.bunnyslayer.util.PlayerUtil;
import pl.bunnyslayer.util.RandomNumber;

import javax.annotation.Nullable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Arena {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final ArenasManager arenasManager = plugin.getArenasManager();
    private final MessagesManager messagesManager = plugin.getMessagesManager();
    private final String name;
    private double duration;
    private double boosterInterval;
    private int bunnyCount = 3;
    private int arenaTask = -1;
    private int boosterTask = -1;
    private boolean started;
    private String musicName;
    private MusicPlayer musicPlayer;
    private List<String> startHours = new ArrayList<>();
    private List<Location> spawnLocations = new ArrayList<>();
    private List<Location> boosterSpawnLocations = new ArrayList<>();
    private final HashMap<String, Double> currentPoints = new HashMap<>();
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

    public void addPoints(String player, double points) {
        currentPoints.put(player, getPlayerCurrentPoints(player) + points);
    }

    public double getPlayerCurrentPoints(String player) {
        return currentPoints.getOrDefault(player, 0.0);
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
        boosterTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
                () -> spawnBooster(getRandomBooster()), 0L, 20L * (long) boosterInterval);
        for(int i = 0; i < bunnyCount; i++) {
            spawnBunny(getRandomCustomBunny());
        }
        if(musicName != null) {
            Location loc = getRandomLocation();
            if(loc != null) {
                musicPlayer = plugin.getMusicManager().createPlayer(musicName, loc);
                if(musicPlayer != null) {
                    musicPlayer.start();
                }
            }
        }
        plugin.getServer().broadcastMessage(MessageFormat.format(messagesManager.getMessage("eventAnnounce"), name));
    }

    public void spawnBunny(CustomBunny bunny) {
        if(bunny == null) {
            return;
        }
        Location spawnLocation = getRandomLocation();
        if(spawnLocation == null) {
            return;
        }
        livingBunnies.add(bunny.spawnBunny(spawnLocation));
    }

    public void spawnBooster(CustomBooster booster) {
        if(booster == null) {
            return;
        }
        Location spawnLocation = getRandomBoosterLocation();
        if(spawnLocation == null) {
            return;
        }
        livingBoosters.add(booster.spawnBooster(spawnLocation, this));
    }

    public void stopArena() {
        if(!this.started) {
            return;
        }
        this.started = false;
        plugin.getServer().getScheduler().cancelTask(arenaTask);
        plugin.getServer().getScheduler().cancelTask(boosterTask);
        killAllBunnies();
        removeAllBoosters();
        double max = 0;
        String maxPlayer = "";
        for(String player : currentPoints.keySet()) {
            if(currentPoints.get(player) > max) {
                max = currentPoints.get(player);
                maxPlayer = player;
            }
            arenasManager.addPoints(player, currentPoints.get(player));
            Player p = PlayerUtil.getPlayer(player);
            if(p != null) {
                p.sendMessage(MessageFormat.format(messagesManager.getMessage("eventMyPoints"),
                        currentPoints.get(player), arenasManager.getPlayerWeekPoints(player)));
            }
        }
        if(!maxPlayer.equals("")) {
            arenasManager.assignReward(maxPlayer, "default");
            plugin.getServer().broadcastMessage(MessageFormat.format(messagesManager.getMessage("eventEnd"),
                    maxPlayer, name, max));
            Player p = PlayerUtil.getPlayer(maxPlayer);
            if(p != null) {
                p.sendMessage(messagesManager.getMessage("newAssignedReward"));
            }
        }
        currentPoints.clear();
        if(musicPlayer != null) {
            plugin.getMusicManager().removePlayer(musicPlayer);
            musicPlayer = null;
        }
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
    public CustomBooster getRandomBooster() {
        int random = RandomNumber.randomInt(1, 100);
        double lastPercentage = 0;
        for(CustomBooster customBooster : customBoosters) {
            if(random > lastPercentage && random <= customBooster.getPercentage() + lastPercentage) {
                return customBooster;
            }
            lastPercentage += customBooster.getPercentage();
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
    public LivingBooster getLivingBooster(Entity entity) {
        for(LivingBooster livingBooster : livingBoosters) {
            if(livingBooster.getBoosterEntity().equals(entity)) {
                return livingBooster;
            }
        }
        return null;
    }

    @NotNull
    public List<String> getSpawnLocationsString() {
        List<String> stringList = new ArrayList<>();
        for(Location spawnLocation : spawnLocations) {
            stringList.add(LocationUtil.locationToString(spawnLocation));
        }
        return stringList;
    }

    @NotNull
    public List<String> getBoosterSpawnLocationsString() {
        List<String> stringList = new ArrayList<>();
        for(Location spawnLocation : boosterSpawnLocations) {
            stringList.add(LocationUtil.locationToString(spawnLocation));
        }
        return stringList;
    }

    public void killAllBunnies() {
        for(LivingBunny bunny : livingBunnies) {
            bunny.getEntity().remove();
        }
        livingBunnies.clear();
    }

    public void removeAllBoosters() {
        for(LivingBooster booster : livingBoosters) {
            booster.remove();
        }
        livingBoosters.clear();
    }

}
