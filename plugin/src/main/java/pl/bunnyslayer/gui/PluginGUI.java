package pl.bunnyslayer.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.bunnyslayer.BunnySlayer;
import pl.bunnyslayer.arena.ArenasManager;
import pl.bunnyslayer.data.MessagesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public abstract class PluginGUI {

    private final BunnySlayer plugin;
    private final ArenasManager arenasManager;
    private final MessagesManager messagesManager;
    private final Player owner;
    private final String inventoryName;
    private final int inventorySize;
    private final Inventory inventory;
    private boolean guiProtected;
    private boolean updating;
    private int updateInterval = 5;
    private int updateTask = -1;

    public PluginGUI(Player owner, String inventoryName, int inventorySize) {
        this.plugin = BunnySlayer.getInstance();
        plugin.getGuiManager().registerInventory(this);
        this.arenasManager = plugin.getArenasManager();
        this.messagesManager = plugin.getMessagesManager();
        this.owner = owner;
        this.inventoryName = inventoryName;
        this.inventorySize = inventorySize;
        inventory = plugin.getServer().createInventory(owner, inventorySize, inventoryName);
        owner.openInventory(inventory);
        onOpen();
    }

    public void setSlot(int slot, Material material, String name, List<String> lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName(name);
            im.setLore(lore);
        }
        is.setItemMeta(im);
        setSlot(slot, is);
    }

    public void setSlot(int slot, int count, Material material, String name, List<String> lore) {
        ItemStack is = new ItemStack(material);
        is.setAmount(count);
        ItemMeta im = is.getItemMeta();
        if(im != null) {
            im.setDisplayName(name);
            im.setLore(lore);
        }
        is.setItemMeta(im);
        setSlot(slot, is);
    }

    public void setSlot(int slot, ItemStack is) {
        inventory.setItem(slot, is);
    }

    public List<String> getLore(String... loreLine) {
        List<String> lore = new ArrayList<>(Arrays.asList(loreLine));
        List<String> parsedLore = new ArrayList<>();
        for(String line : lore) {
            String[] split = line.split("%nl%");
            parsedLore.addAll(Arrays.asList(split));
        }
        return parsedLore;
    }

    public void setSlotEnchanted(int slot, boolean enchanted) {
        ItemStack is = inventory.getItem(slot);
        if(is == null) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if(im == null) {
            return;
        }
        if(enchanted) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            im.removeEnchant(Enchantment.DURABILITY);
            im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        is.setItemMeta(im);
        setSlot(slot, is);
    }

    public void setGUIProtected(boolean protect) {
        guiProtected = protect;
    }

    public void closeInventory() {
        owner.closeInventory();
    }

    public void fillWith(Material material) {
        for(int i = 0; i < inventorySize; i++) {
            setSlot(i, material, " ", new ArrayList<>());
        }
    }

    public void setGUIUpdating(boolean updating) {
        this.updating = updating;
        if(updating) {
            if(updateTask != -1) {
                return;
            }
            updateTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                    plugin, this::onUpdate, 0L, updateInterval);
        } else {
            if(updateTask == -1) {
                return;
            }
            plugin.getServer().getScheduler().cancelTask(updateTask);
        }
    }

    public void onOpen() {

    }

    public void onClose() {
        setGUIUpdating(false);
    }

    public void onClick(int slot) {

    }

    public void onClick(int slot, boolean rightClick) {

    }

    public void onUpdate() {

    }

}
