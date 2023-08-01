package pl.bunnyslayer.music;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MusicSchema {

    private final String name;
    private List<String> musicData = new ArrayList<>();

    public MusicSchema(String name) {
        this.name = name;
    }

}
