package me.maanraj514.contrapvp.utils;

import lombok.experimental.UtilityClass;
import me.maanraj514.contrapvp.object.Pos;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class LocationUtils {

    public Pos locationToPos(Location location){
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        return new Pos(x, y, z, yaw, pitch);
    }

    public Location posToLocation(Pos pos, World world){
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        float yaw = pos.getYaw();
        float pitch = pos.getPitch();
        Location location = new Location(world, x, y, z);
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }
}
