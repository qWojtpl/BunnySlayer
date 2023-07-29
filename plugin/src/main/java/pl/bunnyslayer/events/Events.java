package pl.bunnyslayer.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.boosters.LivingBooster;
import pl.bunnyslayer.bunnies.LivingBunny;
import pl.bunnyslayer.gui.GUIManager;
import pl.bunnyslayer.gui.PluginGUI;

public class Events implements Listener {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final ArenasManager arenasManager = plugin.getArenasManager();
    private final GUIManager guiManager = plugin.getGuiManager();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if(!(event.getEntity() instanceof Rabbit)) {
            return;
        }
        if(!(event.getDamager() instanceof Player)) {
            return;
        }
        Arena arena = arenasManager.getByBunny(event.getEntity());
        if(arena == null) {
            return;
        }
        event.setCancelled(true);
        LivingBunny bunny = arena.getLivingBunny((LivingEntity) event.getEntity());
        // Adding points logic
        arena.getLivingBunnies().remove(bunny);
        event.getEntity().remove();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        PluginGUI gui = guiManager.getGUIByInventory(event.getClickedInventory());
        if(gui == null) {
            return;
        }
        gui.onClick(event.getSlot());
        gui.onClick(event.getSlot(), event.isRightClick());
        if(gui.isGuiProtected()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        PluginGUI gui = guiManager.getGUIByInventory(event.getInventory());
        if(gui == null) {
            return;
        }
        if(gui.isGuiProtected()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        guiManager.removeInventory(guiManager.getGUIByInventory(event.getInventory()));
    }

}
