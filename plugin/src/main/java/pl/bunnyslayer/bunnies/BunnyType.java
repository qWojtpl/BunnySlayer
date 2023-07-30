package pl.bunnyslayer.bunnies;

import org.jetbrains.annotations.NotNull;

public enum BunnyType {

    NORMAL,
    KILLER;

    public static BunnyType getType(@NotNull String name) {
        try {
            return BunnyType.valueOf(name);
        } catch(IllegalArgumentException e) {
            return NORMAL;
        }
    }

}
