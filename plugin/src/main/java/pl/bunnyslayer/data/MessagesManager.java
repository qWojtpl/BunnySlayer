package pl.bunnyslayer.data;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Getter
public class MessagesManager {

    private final HashMap<String, String> messages = new HashMap<>();

    public void addMessage(String key, String value) {
        messages.put(key, value);
    }

    @NotNull
    public String getMessage(String key) {
        return messages.getOrDefault(key, key)
                .replace("&", "§")
                .replace("%prefix%", getPrefix());
    }

    @NotNull
    public String getPrefix() {
        return messages.getOrDefault("prefix", "%prefix%")
                .replace("&", "§");
    }

    public void clearMessages() {
        messages.clear();
    }

}
