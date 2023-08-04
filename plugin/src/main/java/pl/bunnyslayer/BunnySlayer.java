package pl.bunnyslayer;

import lombok.Getter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.commands.CommandHelper;
import pl.bunnyslayer.commands.Commands;
import pl.bunnyslayer.data.DataHandler;
import pl.bunnyslayer.data.MessagesManager;
import pl.bunnyslayer.events.Events;
import pl.bunnyslayer.gui.GUIManager;
import pl.bunnyslayer.music.MusicManager;
import pl.bunnyslayer.permissions.PermissionManager;

@Getter
public final class BunnySlayer extends JavaPlugin {

    private static BunnySlayer main;
    private MusicManager musicManager;
    private ArenasManager arenasManager;
    private PermissionManager permissionManager;
    private MessagesManager messagesManager;
    private Commands commands;
    private CommandHelper commandHelper;
    private DataHandler dataHandler;
    private GUIManager guiManager;
    private Events events;

    @Override
    public void onEnable() {
        main = this;
        this.musicManager = new MusicManager();
        this.arenasManager = new ArenasManager();
        this.permissionManager = new PermissionManager();
        this.messagesManager = new MessagesManager();
        this.commands = new Commands();
        this.commandHelper = new CommandHelper();
        this.dataHandler = new DataHandler();
        this.guiManager = new GUIManager();
        this.events = new Events();
        PluginCommand command = getCommand("bunnyslayer");
        if(command != null) {
            command.setExecutor(commands);
            command.setTabCompleter(commandHelper);
        }
        getServer().getPluginManager().registerEvents(events, this);
        dataHandler.loadAll();
        arenasManager.setDataHandler(dataHandler);
        arenasManager.startTask();
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        for(Arena arena : arenasManager.getArenas()) {
            arena.stopArena();
        }
        guiManager.closeAllInventories();
        dataHandler.saveAll();
        getLogger().info("Disabled.");
    }

    public static BunnySlayer getInstance() {
        return main;
    }

}
