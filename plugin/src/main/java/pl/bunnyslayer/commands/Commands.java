package pl.bunnyslayer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;

public class Commands implements CommandExecutor {

    private final BunnySlayer plugin = BunnySlayer.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("reload")) {
                for(Arena arena : plugin.getArenasManager().getArenas()) {
                    arena.stopArena();
                }
                plugin.getDataHandler().loadAll();
                return true;
            }
        }
        plugin.getArenasManager().getByName("default").startArena();
        return true;
    }

}
