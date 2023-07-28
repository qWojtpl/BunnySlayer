package pl.bunnyslayer.boosters;

import lombok.Getter;
import org.bukkit.entity.Item;

@Getter
public class LivingBooster {

    private final Item boosterItem;

    public LivingBooster(Item boosterItem) {
        this.boosterItem = boosterItem;
    }

}
