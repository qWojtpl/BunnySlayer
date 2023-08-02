package pl.bunnyslayer.events;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.util.Vector;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.bunnies.LivingBunny;
import pl.bunnyslayer.gui.GUIManager;
import pl.bunnyslayer.gui.PluginGUI;
import pl.bunnyslayer.util.PlayerUtil;

public class Events implements Listener {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final ArenasManager arenasManager = plugin.getArenasManager();
    private final GUIManager guiManager = plugin.getGuiManager();

    @EventHandler
    public void onBunnyDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Rabbit)) {
            return;
        }
        Arena arena = arenasManager.getByBunny(event.getEntity());
        if(arena == null) {
            return;
        }
        LivingBunny bunny = arena.getLivingBunny((LivingEntity) event.getEntity());
        if(bunny == null) {
            return;
        }
        event.setCancelled(true);
        if(!(event.getDamager() instanceof Player)) {
            return;
        }
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        arena.addPoints(event.getDamager().getName(), bunny.getExperience());
        arena.getLivingBunnies().remove(bunny);
        event.getEntity().remove();
        arena.spawnBunny(arena.getRandomCustomBunny());
        PlayerUtil.sendActionBarMessage((Player) event.getDamager(), "Your points is now "
                + arena.getPlayerCurrentPoints(event.getDamager().getName()));
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Rabbit)) {
            return;
        }
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Arena arena = arenasManager.getByBunny(event.getDamager());
        if(arena == null) {
            return;
        }
        LivingBunny bunny = arena.getLivingBunny((LivingEntity) event.getDamager());
        if(bunny == null) {
            return;
        }
        event.setCancelled(true);
        event.getEntity().setVelocity(new Vector(0, bunny.getKnockBack(), 0));
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
