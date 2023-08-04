package pl.bunnyslayer.permissions;

import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import pl.bunnyslayer.BunnySlayer;

import java.util.HashMap;

public class PermissionManager {

    private final BunnySlayer plugin = BunnySlayer.getInstance();
    private final HashMap<String, Permission> permissions = new HashMap<>();

    public void addPermission(String name, String permission) {
        if(permissions.containsKey(name)) {
            removePermission(name);
        }
        Permission perm = new Permission(permission);
        plugin.getServer().getPluginManager().addPermission(perm);
        permissions.put(name, perm);
    }

    public void removePermission(String name) {
        if(!permissions.containsKey(name)) {
            return;
        }
        plugin.getServer().getPluginManager().removePermission(permissions.get(name));
        permissions.remove(name);
    }

    public void clearPermissions() {
        for(String permName : permissions.keySet()) {
            plugin.getServer().getPluginManager().removePermission(permissions.get(permName));
        }
        permissions.clear();
    }

    @NotNull
    public Permission getPermission(String name) {
        return permissions.getOrDefault(name, new Permission(""));
    }

}
