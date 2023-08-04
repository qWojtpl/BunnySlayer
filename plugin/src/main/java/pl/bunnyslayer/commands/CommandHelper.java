package pl.bunnyslayer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;

import java.util.ArrayList;
import java.util.List;

public class CommandHelper implements TabCompleter {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final ArenasManager arenasManager = plugin.getArenasManager();
    private final Commands commands = plugin.getCommands();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
            if(hasPermission(sender, "reload")) {
                completions.add("reload");
            }
            if(hasPermission(sender, "startArena")) {
                completions.add("start");
            }
            if(hasPermission(sender, "stopArena")) {
                completions.add("stop");
            }
            if(hasPermission(sender, "arenaInfo")) {
                completions.add("info");
            }
            if(hasPermission(sender, "nextEvent")) {
                completions.add("event");
            }
        } else if(args.length == 2) {
            if(
                (args[0].equalsIgnoreCase("start") && hasPermission(sender, "startArena")) ||
                (args[0].equalsIgnoreCase("stop") && hasPermission(sender, "stopArena")) ||
                (args[0].equalsIgnoreCase("info") && hasPermission(sender, "arenaInfo")))
            {
                completions.addAll(getArenaNames());
            }
        }
        return StringUtil.copyPartialMatches(args[args.length-1], completions, new ArrayList<>());
    }

    private List<String> getArenaNames() {
        List<String> names = new ArrayList<>();
        for(Arena arena : arenasManager.getArenas()) {
            names.add(arena.getName());
        }
        return names;
    }

    private boolean hasPermission(CommandSender sender, String permissionKey) {
        return commands.hasPermission(sender, permissionKey);
    }

}
