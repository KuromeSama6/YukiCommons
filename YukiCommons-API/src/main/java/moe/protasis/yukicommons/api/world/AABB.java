package moe.protasis.yukicommons.api.world;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

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

    public AABB Grown(double x, double y, double z) {
        return new AABB(minX - x, minY - y, minZ - z,
                        maxX + x, maxY + y, maxZ + z);
    }

    public AABB Grown(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AABB(minX - x1, minY - y1, minZ - z1,
            maxX + x2, maxY + y2, maxZ + z2);
    }

    public AABB GrownTo(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new AABB(Math.min(minX, x1), Math.min(minY, y1), Math.min(minZ, z1),
                        Math.max(maxX, x2), Math.max(maxY, y2), Math.max(maxZ, z2));
    }

    public AABB GrownCoordinates(double x, double y, double z) {
        double x0 = x > 0 ? 0 : x;
        double y0 = y > 0 ? 0 : y;
        double z0 = z > 0 ? 0 : z;
        double x1 = x > 0 ? x : 0;
        double y1 = y > 0 ? y : 0;
        double z1 = z > 0 ? z : 0;

        return Grown(x0, y0, z0, x1, y1, z1);
    }

    public Vector GetSize() {
        return new Vector(maxX - minX, maxY - minY, maxZ - minZ);
    }

    public boolean Intersects(AABB other) {
        return this.minX <= other.maxX && this.maxX >= other.minX &&
               this.minY <= other.maxY && this.maxY >= other.minY &&
               this.minZ <= other.maxZ && this.maxZ >= other.minZ;
    }

    public double DistanceX(double x) {
        return x >= this.minX && x <= this.maxX ? 0.0 : Math.min(Math.abs(x - this.minX), Math.abs(x - this.maxX));
    }

    public double DistanceY(double y) {
        return y >= this.minY && y <= this.maxY ? 0.0 : Math.min(Math.abs(y - this.minY), Math.abs(y - this.maxY));
    }

    public double DistanceZ(double z) {
        return z >= this.minZ && z <= this.maxZ ? 0.0 : Math.min(Math.abs(z - this.minZ), Math.abs(z - this.maxZ));
    }

    public AABB Lerp(AABB other, double t) {
        return Lerp(this, other, t);
    }

    public AABB Clone() {
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static AABB Lerp(AABB a, AABB b, double t) {
        return new AABB(
                a.minX + (b.minX - a.minX) * t,
                a.minY + (b.minY - a.minY) * t,
                a.minZ + (b.minZ - a.minZ) * t,
                a.maxX + (b.maxX - a.maxX) * t,
                a.maxY + (b.maxY - a.maxY) * t,
                a.maxZ + (b.maxZ - a.maxZ) * t
        );
    }

    public static AABB Zero() {
        return new AABB(0, 0, 0, 0, 0, 0);
    }
}
