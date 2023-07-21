package pl.bunnyslayer;

import lombok.Getter;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.commands.CommandHelper;
import pl.bunnyslayer.commands.Commands;
import pl.bunnyslayer.data.DataHandler;
import pl.bunnyslayer.events.Events;
import pl.bunnyslayer.gui.GUIManager;

@Getter
public final class BunnySlayer extends JavaPlugin {

    private static BunnySlayer main;
    private ArenasManager arenasManager;
    private CommandHelper commandHelper;
    private Commands commands;
    private DataHandler dataHandler;
    private Events events;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        main = this;
        this.arenasManager = new ArenasManager();
        this.commandHelper = new CommandHelper();
        this.commands = new Commands();
        this.dataHandler = new DataHandler();
        this.events = new Events();
        this.guiManager = new GUIManager();
        PluginCommand command = getCommand("bunnyslayer");
        if(command != null) {
            command.setExecutor(commands);
            command.setTabCompleter(commandHelper);
        }
        getServer().getPluginManager().registerEvents(events, this);
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled.");
    }

    public static BunnySlayer getInstance() {
        return main;
    }

}
