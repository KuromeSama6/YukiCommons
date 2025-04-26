package moe.protasis.yukicommons.api.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AbstractLocation {
    private final String world;
    private final double x, y, z, yaw, pitch;
}
