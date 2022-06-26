package me.maanraj514.contrapvp.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public class MapData {

    @Getter
    public String name;
    @Getter
    public List<String> authors;
    @Setter
    @Getter
    public long lastEdit;

    @Getter
    public List<Pos> spawnPoints;
    @Setter
    @Getter
    public Pos spectatorSpawn;
}
