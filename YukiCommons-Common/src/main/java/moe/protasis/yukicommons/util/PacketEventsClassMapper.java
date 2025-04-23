package moe.protasis.yukicommons.util;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_7_10;
import io.github.classgraph.ClassGraph;
import lombok.Getter;
import moe.protasis.yukicommons.api.IYukiCommons;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

/**
 * Maps NMS packet wrapper classes to their respective packet types.
 */
public class PacketEventsClassMapper {
    private final IYukiCommons plugin;

    public PacketEventsClassMapper(IYukiCommons plugin) {
        this.plugin = plugin;
        var logger = plugin.GetLogger();

        logger.info("Mapping NMS packet wrapper classes to their respective packet types...");

        // begin mapping
        try (var result = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(PacketType.class.getPackageName())
                .scan()
        ) {
            logger.info("Found %d classes.".formatted(result.getAllClasses().size()));
        }

    }

}
