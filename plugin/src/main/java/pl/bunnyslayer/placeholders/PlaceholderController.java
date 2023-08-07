package pl.bunnyslayer.placeholders;

import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.data.DataHandler;
import pl.bunnyslayer.data.MessagesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class PlaceholderController extends PlaceholderExpansion {

    private final BunnySlayer bunnySlayerPlugin = BunnySlayer.getInstance();
    private final ArenasManager arenasManager = bunnySlayerPlugin.getArenasManager();
    private final MessagesManager messagesManager = bunnySlayerPlugin.getMessagesManager();
    private final HashMap<Integer, String> leaderboard = new HashMap<>();
    private DataHandler dataHandler;
    private int updateTask = -1;

    @Override
    public @NotNull String getIdentifier() {
        return "bunnyslayer";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Assasin98980";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if(params.toLowerCase().startsWith("top_")) {
            try {
                String num = params.replace("top_", "");
                int id = Integer.parseInt(num);
                if(id > leaderboard.size() - 1) {
                    return "&c-";
                }
                return leaderboard.get(id);
            } catch(NumberFormatException ignored) {
                return "&cINVALID NUMBER";
            }
        }
        return null;
    }

    public void registerTask() {
        if(updateTask != -1) {
            bunnySlayerPlugin.getServer().getScheduler().cancelTask(updateTask);
        }
        updateTask = bunnySlayerPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(bunnySlayerPlugin, this::updateLeaderboard,
                0L, 20L * dataHandler.getLeaderboardUpdateInterval());
    }

    public void updateLeaderboard() {
        List<String> exclude = new ArrayList<>();
        for(int i = 0; i < dataHandler.getLeaderboardMaxRecords(); i++) {
            String maxPlayer = "";
            double max = 0;
            for(String player : arenasManager.getWeekPoints().keySet()) {
                if(exclude.contains(player)) {
                    continue;
                }
                if(arenasManager.getWeekPoints().get(player) > max) {
                    max = arenasManager.getWeekPoints().get(player);
                    maxPlayer = player;
                }
            }
            if(!maxPlayer.equals("")) {
                leaderboard.put(i, (i + 1) + ". " + maxPlayer + ": " + max);
                exclude.add(maxPlayer);
            }
        }
    }

}
