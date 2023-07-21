package pl.bunnyslayer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.bunnyslayer.BunnySlayer;

import java.util.List;

public class CommandHelper implements TabCompleter {

    private final BunnySlayer plugin = BunnySlayer.getInstance();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }

}
