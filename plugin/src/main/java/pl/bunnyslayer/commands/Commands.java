package pl.bunnyslayer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.data.MessagesManager;
import pl.bunnyslayer.permissions.PermissionManager;

public class Commands implements CommandExecutor {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final PermissionManager permissionManager = plugin.getPermissionManager();
    private final MessagesManager messages = plugin.getMessagesManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("help")) {
                showHelpPage(sender);
            } else if(args[0].equalsIgnoreCase("reload")) {
                reloadCommand(sender);
            } else if(args[0].equalsIgnoreCase("start")) {
                startArena(sender, args);
            } else if(args[0].equalsIgnoreCase("stop")) {
                stopArena(sender, args);
            } else if(args[0].equalsIgnoreCase("info")) {
                getArenaInfo(sender, args);
            } else if(args[0].equalsIgnoreCase("event")) {
                getNextEventInfo(sender, args);
            }
        } else {
            showHelpPage(sender);
        }
        return true;
    }

    public void showHelpPage(CommandSender sender) {
        boolean anyAccess = false;
        if(hasPermission(sender, "reload")) {
            anyAccess = true;
            sender.sendMessage("§6/§2bs §areload §6- §2Reload configuration");
        }
        if(!anyAccess) {
            sender.sendMessage(messages.getMessage("noAccess"));
        }
    }

    public void startArena(CommandSender sender, String[] args) {

    }

    public void stopArena(CommandSender sender, String[] args) {

    }

    public void getArenaInfo(CommandSender sender, String[] args) {

    }

    public void getNextEventInfo(CommandSender sender, String[] args) {

    }

    public void reloadCommand(CommandSender sender) {
        if(!checkPermission(sender, "reload")) {
            return;
        }
        for(Arena arena : plugin.getArenasManager().getArenas()) {
            arena.stopArena();
        }
        plugin.getDataHandler().saveAll();
        plugin.getDataHandler().loadAll();
        sender.sendMessage(messages.getMessage("reloaded"));
    }

    public boolean checkPermission(CommandSender sender, String permissionKey) {
        if(hasPermission(sender, permissionKey)) {
            sender.sendMessage(messages.getMessage("noPermission"));
            return true;
        }
        return false;
    }

    public boolean hasPermission(CommandSender sender, String permissionKey) {
        return sender.hasPermission(permissionManager.getPermission(permissionKey));
    }

}
