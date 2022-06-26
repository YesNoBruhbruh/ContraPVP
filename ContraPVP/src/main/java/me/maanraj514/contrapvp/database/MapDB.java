package me.maanraj514.contrapvp.database;

import me.maanraj514.contrapvp.object.MapData;
import me.maanraj514.contrapvp.utils.MapDataSerializer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapDB {

    private final Connection connection;

    public MapDB(Connection connection){
        this.connection = connection;
    }

    public MapData loadMapData(String map){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT DATA from mapdata WHERE NAME=?");
            ps.setString(1, map);

            ResultSet results = ps.executeQuery();
            if(results.next()){
                String json = results.getString("DATA");
                return MapDataSerializer.deserialize(json);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void saveMapData(String map, MapData data){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO mapdata (NAME, DATA) VALUES (?, ?) ON DUPLICATE KEY UPDATE DATA=?");
            ps.setString(1, map);
            ps.setString(2, MapDataSerializer.serialize(data));

            ps.setString(3, MapDataSerializer.serialize(data));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
