package moe.protasis.yukicommons.util;

import com.google.gson.JsonParseException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import moe.protasis.yukicommons.api.json.IJsonSerializable;
import moe.protasis.yukicommons.api.json.JsonWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class SimpleLocation implements IJsonSerializable {
    public static final SimpleLocation ZERO = new SimpleLocation(0, 0, 0, 0, 0);

    @Getter
    public final double x, y, z, yaw, pitch;

    public SimpleLocation(JsonWrapper data) {
        this.x = data.GetDouble("x");
        this.y = data.GetDouble("y");
        this.z = data.GetDouble("z");
        this.yaw = data.GetDouble("yaw");
        this.pitch = data.GetDouble("pitch");
    }

    public SimpleLocation(String str) {
        boolean useEntityOffset = str.endsWith("/e");
        if (useEntityOffset) {
            str = str.substring(0, str.length() - 2);
        }

        var args = str.split(",");
        if (args.length < 3)
            throw new JsonParseException("At least 3 arguments are required to deserialize a Location: " + str);

        try {
            var x = Double.parseDouble(args[0]);
            var y = Double.parseDouble(args[1]);
            var z = Double.parseDouble(args[2]);
            var pitch = args.length > 3 ? Float.parseFloat(args[3]) : 0f;
            var yaw = args.length > 4 ? Float.parseFloat(args[4]) : 0f;

            if (useEntityOffset) {
                x += 0.5;
                z += 0.5;
            }

            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;

        } catch (NumberFormatException e) {
            throw new JsonParseException("Failed to parse Location coordinates: " + str, e);
        }
    }

    public SimpleLocation WithRotation(double yaw, double pitch) {
        return new SimpleLocation(x, y, z, yaw, pitch);
    }

    public SimpleLocation WithPosition(double x, double y, double z) {
        return new SimpleLocation(x, y, z, (float)yaw, (float)pitch);
    }

    public SimpleLocation WithPosition(SimpleLocation other) {
        return new SimpleLocation(other.x, other.y, other.z, (float)yaw, (float)pitch);
    }

    public SimpleLocation WithRotation(SimpleLocation other) {
        return new SimpleLocation(x, y, z, (float)other.yaw, (float)other.pitch);
    }

    public SimpleLocation With(Double x, Double y, Double z, Double yaw, Double pitch) {
        return new SimpleLocation(
                x != null ? x : this.x,
                y != null ? y : this.y,
                z != null ? z : this.z,
                yaw != null ? yaw : this.yaw,
                pitch != null ? pitch : this.pitch
        );
    }

    public double Distance(SimpleLocation other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2) + Math.pow(this.z - other.z, 2));
    }

    public Vector GetDirection() {
        Vector vector = new Vector();
        double rotX = getYaw();
        double rotY = getPitch();
        vector.setY(-Math.sin(Math.toRadians(rotY)));
        double xz = Math.cos(Math.toRadians(rotY));
        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));
        return vector;
    }

    public Location ToBukkitLocation(World world) {
        return new Location(world, x, y, z, (float)yaw, (float)pitch);
    }

    public Location ToBukkitLocation(String world) {
        return ToBukkitLocation(Bukkit.getWorld(world));
    }

    public static SimpleLocation FromBukkitLocation(Location location) {
        return new SimpleLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}
