package pl.bunnyslayer.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.boosters.CustomBooster;
import pl.bunnyslayer.bunnies.CustomBunny;
import pl.bunnyslayer.data.MessagesManager;
import pl.bunnyslayer.gui.PluginGUI;
import pl.bunnyslayer.gui.list.RewardGUI;
import pl.bunnyslayer.permissions.PermissionManager;

@SuppressWarnings("deprecation")
public class Commands implements CommandExecutor {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final PermissionManager permissionManager = plugin.getPermissionManager();
    private final MessagesManager messages = plugin.getMessagesManager();
    private final ArenasManager arenasManager = plugin.getArenasManager();

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
                getNextEventInfo(sender);
            } else if(args[0].equalsIgnoreCase("rewards")) {
                showRewards(sender);
            } else {
                showHelpPage(sender);
            }
        } else {
            showHelpPage(sender);
        }
        return true;
    }

    public void showHelpPage(CommandSender sender) {
        String helpStr = "";
        boolean anyAccess = false;
        if(hasPermission(sender, "reload")) {
            anyAccess = true;
            helpStr += "§6/§2bs §areload §6- §2Reloads configuration\n";
        }
        if(hasPermission(sender, "startArena")) {
            anyAccess = true;
            helpStr += "§6/§2bs §astart §6<§aarena§6> §6- §2Starts arena\n";
        }
        if(hasPermission(sender, "stopArena")) {
            anyAccess = true;
            helpStr += "§6/§2bs §astop §6<§aarena§6> §6- §2Stops arena\n";
        }
        if(hasPermission(sender, "arenaInfo")) {
            anyAccess = true;
            helpStr += "§6/§2bs §ainfo §6<§aarena§6> §6- §2Gets information about arena\n";
        }
        if(hasPermission(sender, "nextEvent")) {
            anyAccess = true;
            helpStr += "§6/§2bs §aevent §6- §2Shows information about event start hours\n";
        }
        if(hasPermission(sender, "seeRewards")) {
            if(!anyAccess && (sender instanceof Player)) {
                showRewards(sender);
                return;
            } else {
                helpStr += "§6/§2bs §arewards §6- §2Shows rewards GUI\n";
            }
            anyAccess = true;
        }
        if(!anyAccess) {
            helpStr += messages.getMessage("noAccess");
        }
        sender.sendMessage("§6========= §2BunnySlayer §6=========");
        sender.sendMessage(" ");
        sender.sendMessage(helpStr);
        sender.sendMessage(" ");
        sender.sendMessage("§6========= §2BunnySlayer §6=========");
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

    public void startArena(CommandSender sender, String[] args) {
        if(!checkPermission(sender, "startArena")) {
            return;
        }
        if(args.length < 2) {
            sender.sendMessage(messages.getMessage("correctUsage") + "/bs start <arena>");
            return;
        }
        Arena arena = arenasManager.getByName(args[1]);
        if(arena == null) {
            sender.sendMessage(messages.getMessage("notFoundArena") + args[1]);
            return;
        }
        if(arena.isStarted()) {
            sender.sendMessage(messages.getMessage("arenaAlreadyStarted"));
            return;
        }
        arena.startArena();
        sender.sendMessage(messages.getMessage("arenaSuccessfullyStarted") + args[1]);
    }

    public void stopArena(CommandSender sender, String[] args) {
        if(!checkPermission(sender, "stopArena")) {
            return;
        }
        if(args.length < 2) {
            sender.sendMessage(messages.getMessage("correctUsage") + "/bs stop <arena>");
            return;
        }
        Arena arena = arenasManager.getByName(args[1]);
        if(arena == null) {
            sender.sendMessage(messages.getMessage("notFoundArena") + args[1]);
            return;
        }
        if(!arena.isStarted()) {
            sender.sendMessage(messages.getMessage("arenaNotStarted"));
            return;
        }
        arena.stopArena();
        sender.sendMessage(messages.getMessage("arenaSuccessfullyStopped") + args[1]);
    }

    public void getArenaInfo(CommandSender sender, String[] args) {
        if(!checkPermission(sender, "arenaInfo")) {
            return;
        }
        if(args.length < 2) {
            sender.sendMessage(messages.getMessage("correctUsage") + "/bs info <arena>");
            return;
        }
        Arena arena = arenasManager.getByName(args[1]);
        if(arena == null) {
            sender.sendMessage(messages.getMessage("notFoundArena") + args[1]);
            return;
        }
        sender.sendMessage("§6========= §2BunnySlayer §6=========");
        sender.sendMessage(" ");
        sender.sendMessage("§aName: §6" + arena.getName());
        sender.sendMessage("§aStart hours: §6" + String.join("§2, §6", arena.getStartHours()));
        sender.sendMessage("§aDuration: §6" + arena.getDuration() + " minutes");
        if(arena.getMusicName() != null) {
            sender.sendMessage("§aMusic: §6" + arena.getMusicName());
        }
        sender.sendMessage("§aBunny count: §6" + arena.getBunnyCount());
        BaseComponent[] components = new BaseComponent[arena.getCustomBunnies().size() + 1];
        components[0] = new TextComponent("§aBunnies: ");
        int i = 0;
        for(CustomBunny customBunny : arena.getCustomBunnies()) {
            i++;
            TextComponent component = new TextComponent("§6" + i + "§2(§6" + customBunny.getPercentage() + "%§2) ");
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(
                            "§7Percentage: §6" + customBunny.getPercentage() + "%\n" +
                            "§7Speed: §6" + customBunny.getSpeed() + "\n" +
                            "§7Experience: §6" + customBunny.getExperience() + "\n" +
                            "§7Type: §6" + customBunny.getBunnyType().name() + "\n" +
                            "§7Launch force: §6" + customBunny.getLaunchForce()).create()));
            components[i] = component;
        }
        if(i > 1) {
            sender.spigot().sendMessage(components);
        }
        TextComponent locComponent = new TextComponent("§aBunny spawn locations: §6" + arena.getSpawnLocations().size());
        locComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7" + String.join("§6,\n§7", arena.getSpawnLocationsString())).create()));
        sender.spigot().sendMessage(locComponent);
        sender.sendMessage("§aBooster spawn interval: §6" + arena.getBoosterInterval() + " seconds");
        components = new BaseComponent[arena.getCustomBoosters().size() + 1];
        components[0] = new TextComponent("§aBoosters: ");
        i = 0;
        for(CustomBooster customBooster : arena.getCustomBoosters()) {
            i++;
            TextComponent component = new TextComponent("§6" + customBooster.getName().replace("&", "§") + "§2(§6" + customBooster.getPercentage() + "%§2) ");
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(
                            "§7Name: §6" + customBooster.getName().replace("&", "§") + "\n" +
                            "§7Percentage: §6" + customBooster.getPercentage() + "%\n" +
                            "§7Item: §6" + customBooster.getItem().getType().name() + "\n" +
                            "§7Effects: §6" + String.join("§2, §6", customBooster.getEffects())).create()));
            components[i] = component;
        }
        if(i > 1) {
            sender.spigot().sendMessage(components);
        }
        locComponent = new TextComponent("§aBooster spawn locations: §6" + arena.getBoosterSpawnLocations().size());
        locComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7" + String.join("§6,\n§7", arena.getBoosterSpawnLocationsString())).create()));
        sender.spigot().sendMessage(locComponent);
        sender.sendMessage(" ");
        sender.sendMessage("§6========= §2BunnySlayer §6=========");
    }

    public void getNextEventInfo(CommandSender sender) {
        if(!checkPermission(sender, "nextEvent")) {
            return;
        }
        sender.sendMessage("§6========= §2BunnySlayer §6=========");
        sender.sendMessage(" ");
        for(Arena arena : arenasManager.getArenas()) {
            String color = "§7";
            if(arena.isStarted()) {
                color = "§a";
            }
            sender.sendMessage(color + arena.getName() + " §e- §6" + String.join("§e, §6", arena.getStartHours()));
        }
        sender.sendMessage(" ");
        sender.sendMessage("§6========= §2BunnySlayer §6=========");
    }

    public void showRewards(CommandSender sender) {
        if(!checkPermission(sender, "seeRewards")) {
            return;
        }
        if(!(sender instanceof Player)) {
            sender.sendMessage(messages.getMessage("mustBePlayer"));
            return;
        }
        new RewardGUI((Player) sender, messages.getMessage("rewardMenuTitle"));
    }

    public boolean checkPermission(CommandSender sender, String permissionKey) {
        if(!hasPermission(sender, permissionKey)) {
            sender.sendMessage(messages.getMessage("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasPermission(CommandSender sender, String permissionKey) {
        return sender.hasPermission(permissionManager.getPermission(permissionKey));
    }

}
