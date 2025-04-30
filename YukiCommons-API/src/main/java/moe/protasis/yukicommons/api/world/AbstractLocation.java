package moe.protasis.yukicommons.api.world;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AbstractLocation {
    private String world;
    private double x, y, z, yaw, pitch;
}
