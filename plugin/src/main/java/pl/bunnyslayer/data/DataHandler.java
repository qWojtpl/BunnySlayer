package pl.bunnyslayer.data;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.boosters.CustomBooster;
import pl.bunnyslayer.bunnies.BunnyType;
import pl.bunnyslayer.bunnies.CustomBunny;
import pl.bunnyslayer.util.LocationUtil;

import java.io.File;
import java.io.IOException;

@Getter
public class DataHandler {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final ArenasManager arenasManager = plugin.getArenasManager();
    private final MessagesManager messagesManager = plugin.getMessagesManager();
    private boolean pluginEnabled;
    private String payday;
    private YamlConfiguration data;

    public void loadAll() {
        loadConfig();
        loadMessages();
        loadData();
    }

    public void loadConfig() {
        arenasManager.clearArenas();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(getConfigFile());
        pluginEnabled = yml.getBoolean("config.enabled");
        if(!pluginEnabled) {
            return;
        }
        payday = yml.getString("config.payday", "SUNDAY").toUpperCase();
        ConfigurationSection arenaSection = yml.getConfigurationSection("arenas");
        if(arenaSection != null) {
            for(String arenaName : arenaSection.getKeys(false)) {
                String path = "arenas." + arenaName + ".";
                Arena arena = new Arena(arenaName);
                arenasManager.addArena(arena);
                arena.setDuration(yml.getDouble(path + "duration"));
                arena.setBoosterInterval(yml.getDouble(path + "boosterInterval"));
                arena.setStartHours(yml.getStringList(path + "startHours"));
                arena.setSpawnLocations(LocationUtil.parseList(yml.getStringList(path + "spawnLocations")));
                arena.setBoosterSpawnLocations(LocationUtil.parseList(yml.getStringList(path + "boosterSpawnLocations")));
                ConfigurationSection bunniesSection = yml.getConfigurationSection(path + "bunnies");
                if(bunniesSection != null) {
                    double totalPercent = 0;
                    for(String bunnyKey : bunniesSection.getKeys(false)) {
                        String bunnyPath = path + "bunnies." + bunnyKey + ".";
                        CustomBunny bunny = new CustomBunny();
                        bunny.setBunnyType(BunnyType.getType(yml.getString(bunnyPath + "type", "NORMAL")));
                        bunny.setSpeed(yml.getDouble(bunnyPath + "speed"));
                        bunny.setPercentage(yml.getDouble(bunnyPath + "percentage"));
                        bunny.setExperience(yml.getDouble(bunnyPath + "exp"));
                        bunny.setKnockBack(yml.getDouble(bunnyPath + "knockBack"));
                        arena.addCustomBunny(bunny);
                        totalPercent += bunny.getPercentage();
                    }
                    if(totalPercent != 100) {
                        arena.getCustomBunnies().clear();
                        plugin.getLogger().severe("Bunnies percent is not equals with 100! Clearing bunny list...");
                    }
                }
                ConfigurationSection boostersSection = yml.getConfigurationSection(path + "boosters");
                if(boostersSection != null) {
                    double totalPercent = 0;
                    for(String boosterKey : boostersSection.getKeys(false)) {
                        String boosterPath = path + "boosters." + boosterKey + ".";
                        CustomBooster booster = new CustomBooster();
                        booster.setName(yml.getString(boosterPath + "name"));
                        booster.setPercentage(yml.getDouble(boosterPath + "percentage"));
                        Material m = Material.getMaterial(yml.getString(boosterPath + "item", "DIRT"));
                        if(m == null) {
                            m = Material.DIRT;
                        }
                        ItemStack is = new ItemStack(m);
                        is.setAmount(64);
                        booster.setItem(is);
                        booster.setEffects(yml.getStringList(boosterPath + "effects"));
                        arena.addCustomBooster(booster);
                        totalPercent += booster.getPercentage();
                    }
                    if(totalPercent != 100) {
                        arena.getCustomBoosters().clear();
                        plugin.getLogger().severe("Boosters percent is not equals with 100! Clearing booster list...");
                    }
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
        ConfigurationSection pointsSection = data.getConfigurationSection("points");
        if(pointsSection != null) {
            for(String player : pointsSection.getKeys(false)) {
                arenasManager.getWeekPoints().put(player, data.getDouble("points." + player));
            }
        }
    }

    public void saveAll() {
        try {
            data.save(getDataFile());
        } catch(IOException e) {
            plugin.getLogger().severe("Cannot save data.yml: " + e.getMessage());
        }
    }

    public void savePoints(String player, Double points) {
        data.set("points." + player, points);
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
