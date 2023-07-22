package pl.bunnyslayer.arena;

public enum BunnyType {

    NORMAL,
    KILLER;

    public static BunnyType getType(String name) {
        try {
            return BunnyType.valueOf(name);
        } catch(IllegalArgumentException e) {
            return NORMAL;
        }
    }

}
