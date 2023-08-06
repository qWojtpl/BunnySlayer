package pl.bunnyslayer.events;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.Arena;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.bunnies.LivingBunny;
import pl.bunnyslayer.gui.GUIManager;
import pl.bunnyslayer.gui.PluginGUI;
import pl.bunnyslayer.gui.list.RewardGUI;
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
        event.getEntity().getWorld().spawnParticle(
                Particle.BLOCK_CRACK,
                event.getEntity().getLocation(),
                30,
                Material.SNOW_BLOCK.createBlockData());
        ((Player) event.getDamager()).playSound(
                event.getDamager().getLocation(),
                Sound.ENTITY_GENERIC_EXPLODE,
                0.75F,
                (float) (2 * bunny.getExperience()));
        arena.addPoints(event.getDamager().getName(), bunny.getExperience());
        arena.getLivingBunnies().remove(bunny);
        event.getEntity().remove();
        arena.spawnBunny(arena.getRandomCustomBunny());
        PlayerUtil.sendActionBarMessage((Player) event.getDamager(), "Your points are now "
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
        event.getEntity().setVelocity(new Vector(0, bunny.getLaunchForce(), 0));
    }

    @EventHandler
    public void onNPCClick(PlayerInteractAtEntityEvent event) {
        if(!event.getRightClicked().hasMetadata("NPC")) {
            return;
        }
        if(!event.getRightClicked().getName().equals(plugin.getDataHandler().getNpcName().replace("&", "§"))) {
            return;
        }
        new RewardGUI(event.getPlayer(), plugin.getMessagesManager().getMessage("rewardMenuTitle"));
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
