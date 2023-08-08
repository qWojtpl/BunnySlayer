package pl.bunnyslayer.gui.list;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.bunnyslayer.gui.PluginGUI;

import java.util.ArrayList;
import java.util.List;

public class RewardGUI extends PluginGUI {

    private int currentOffset;
    private List<Integer> rewardSlots;

    public RewardGUI(Player owner, String inventoryName) {
        super(owner, inventoryName, 54);
    }

    @Override
    public void onOpen() {
        setGUIProtected(true);
        currentOffset = 0;
        updateRewards();
    }

    @Override
    public void onClick(int slot) {
        if(slot == 46) {
            previousPage();
        } else if(slot == 49) {
            claimAllRewards();
        } else if(slot == 52) {
            nextPage();
        } else if(rewardSlots.contains(slot)) {
            getArenasManager().receiveReward(getOwner().getName(), rewardSlots.indexOf(slot));
            getOwner().playSound(getOwner(), Sound.ENTITY_RABBIT_AMBIENT, 10.0F, 0.5F);
            updateRewards();
        }
    }

    private void previousPage() {
        if(currentOffset == 0) {
            getOwner().playSound(getOwner(), Sound.ENTITY_RABBIT_HURT, 10.0F, 0.5F);
            return;
        }
        currentOffset -= 28;
        getOwner().playSound(getOwner(), Sound.ENTITY_RABBIT_JUMP, 10.0F, 0.5F);
        updateRewards();
    }

    private void claimAllRewards() {
        while(getArenasManager().getPlayerRewards(getOwner().getName()).size() > 0) {
            getArenasManager().receiveReward(getOwner().getName(), 0);
        }
        getOwner().playSound(getOwner(), Sound.ENTITY_RABBIT_DEATH, 10.0F, 3.5F);
        updateRewards();
    }

    private void nextPage() {
        if(getArenasManager().getPlayerRewards(getOwner().getName()).size() - 1 < currentOffset + 28) {
            getOwner().playSound(getOwner(), Sound.ENTITY_RABBIT_HURT, 10.0F, 0.5F);
            return;
        }
        currentOffset += 28;
        getOwner().playSound(getOwner(), Sound.ENTITY_RABBIT_JUMP, 10.0F, 0.5F);
        updateRewards();
    }

    public void updateRewards() {
        rewardSlots = new ArrayList<>();
        fillWith(Material.BLACK_STAINED_GLASS_PANE);
        List<ItemStack> rewards = new ArrayList<>(getArenasManager().getPlayerRewards(getOwner().getName()));
        List<Integer> availableSlots = new ArrayList<>();
        int[] protectedSlots = new int[]{17, 18, 26, 27, 35, 36};
        for(int i = 10; i <= 43; i++) {
            boolean found = false;
            for(int protectedSlot : protectedSlots) {
                if(protectedSlot == i) {
                    found = true;
                    break;
                }
            }
            if(found) {
                continue;
            }
            setSlot(i, Material.WHITE_STAINED_GLASS_PANE, " ", getLore(" "));
            availableSlots.add(i);
        }
        setSlot(46, Material.ARROW, getMessagesManager().getMessage("previousPage"),
                getLore(getMessagesManager().getMessage("previousPageLore")));
        setSlot(49, Material.CHEST, getMessagesManager().getMessage("claimAllRewards"),
                getLore(getMessagesManager().getMessage("claimAllRewardsLore")));
        setSlot(52, Material.ARROW, getMessagesManager().getMessage("nextPage"),
                getLore(getMessagesManager().getMessage("nextPageLore")));
        int i = 0;
        int j = 0;
        for(ItemStack is : rewards) {
            is = is.clone();
            if(j < currentOffset) {
                j++;
                continue;
            }
            if(i > availableSlots.size() - 1) {
                break;
            }
            ItemMeta im = is.getItemMeta();
            if(im != null) {
                List<String> lore = im.getLore();
                if(lore == null) {
                    lore = new ArrayList<>();
                }
                lore.add(" ");
                lore.add(getMessagesManager().getMessage("clickToReceive"));
                im.setLore(lore);
                is.setItemMeta(im);
            }
            setSlot(availableSlots.get(i), is);
            rewardSlots.add(availableSlots.get(i));
            i++;
        }
    }

}
