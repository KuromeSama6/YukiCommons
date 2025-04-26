package moe.protasis.yukicommons.api.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AABB {
    private final double minX, minY, minZ;
    private final double maxX, maxY, maxZ;

    public boolean Contains(double x, double y, double z) {
        return x >= minX && x <= maxX &&
               y >= minY && y <= maxY &&
               z >= minZ && z <= maxZ;
    }

    public static AABB Zero() {
        return new AABB(0, 0, 0, 0, 0, 0);
    }
}
