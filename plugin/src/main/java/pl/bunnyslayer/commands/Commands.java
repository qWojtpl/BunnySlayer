package pl.bunnyslayer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;

public class Commands implements CommandExecutor {

    private final BunnySlayer plugin = BunnySlayer.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        plugin.getArenasManager().getByName("default").startArena();
        return true;
    }

}
