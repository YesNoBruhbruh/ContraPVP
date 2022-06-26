package me.maanraj514.contrapvp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import me.maanraj514.contrapvp.object.MapData;

@UtilityClass
public class MapDataSerializer {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String serialize(MapData data){
        return gson.toJson(data);
    }

    public MapData deserialize(String json){
        return gson.fromJson(json, MapData.class);
    }
}
