package pl.bunnyslayer.data;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class MessagesManager {

    private final HashMap<String, String> messages = new HashMap<>();

    public void addMessage(String key, String value) {
        messages.put(key, value);
    }

    public String getMessage(String key) {
        return messages.getOrDefault(key, key);
    }

    public void clearMessages() {
        messages.clear();
    }

}
