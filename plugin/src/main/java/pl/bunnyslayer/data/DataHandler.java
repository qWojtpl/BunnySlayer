package pl.bunnyslayer.data;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.arena.BunnyType;
import pl.bunnyslayer.arena.CustomBunny;
import pl.bunnyslayer.util.LocationUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DataHandler {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final ArenasManager arenasManager = plugin.getArenasManager();
    private final MessagesManager messagesManager = plugin.getMessagesManager();
    private YamlConfiguration data;

    public void loadAll() {
        loadConfig();
        loadMessages();
        loadData();
    }

    public void loadConfig() {
        arenasManager.clearArenas();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(getConfigFile());
        ConfigurationSection arenaSection = yml.getConfigurationSection("arenas");
        if(arenaSection != null) {
            for(String arenaName : arenaSection.getKeys(false)) {
                String path = "arenas." + arenaName + ".";
                Arena arena = new Arena(arenaName);
                arenasManager.addArena(arena);
                arena.setDuration(yml.getInt(path + "duration"));
                arena.setStartHours(yml.getStringList(path + "startHours"));
                List<Location> spawnLocations = new ArrayList<>();
                for(String locationString : yml.getStringList(path + "spawnLocations")) {
                    Location loc = LocationUtil.parseLocation(locationString);
                    if(loc == null) {
                        continue;
                    }
                    spawnLocations.add(loc);
                }
                arena.setSpawnLocations(spawnLocations);
                ConfigurationSection bunniesSection = yml.getConfigurationSection(path + "bunnies");
                if(bunniesSection == null) {
                    continue;
                }
                for(String bunnyKey : bunniesSection.getKeys(false)) {
                    String bunnyPath = path + "bunnies." + bunnyKey + ".";
                    CustomBunny bunny = new CustomBunny();
                    bunny.setBunnyType(BunnyType.getType(yml.getString(bunnyPath + "type")));
                    bunny.setSpeed(yml.getDouble(bunnyPath + "speed"));
                    bunny.setPercentage(yml.getDouble(bunnyPath + "percentage"));
                    bunny.setExperience(yml.getDouble(bunnyPath + "exp"));
                    bunny.setKnockBack(yml.getDouble(bunnyPath + "knockBack"));
                    bunny.setDamage(yml.getDouble(bunnyPath + "damage"));
                    arena.addCustomBunny(bunny);
                }
            }
        }
    }

    public void loadMessages() {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(getMessagesFile());
        ConfigurationSection messagesSection = yml.getConfigurationSection("messages");
        if(messagesSection == null) {
            return;
        }
        for(String key : messagesSection.getKeys(false)) {
            messagesManager.addMessage(key, yml.getString("messages." + key));
        }
    }

    public void loadData() {
        data = null;
        data = YamlConfiguration.loadConfiguration(getDataFile());
    }

    public File getConfigFile() {
        return getFile("config.yml");
    }

    public File getMessagesFile() {
        return getFile("messages.yml");
    }

    public File getDataFile() {
        return getFile("data.yml");
    }

    public File getFile(String resourceName) {
        File file = new File(plugin.getDataFolder(), resourceName);
        if(!file.exists()) {
            plugin.saveResource(resourceName, false);
        }
        return file;
    }

}
