package me.maanraj514.contrapvp.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Pos {

    @Getter
    private double x;
    @Getter
    private double y;
    @Getter
    private double z;
    @Getter
    private float yaw;
    @Getter
    private float pitch;
}
