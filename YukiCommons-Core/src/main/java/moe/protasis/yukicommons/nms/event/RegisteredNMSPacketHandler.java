package moe.protasis.yukicommons.nms.event.impl;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import moe.protasis.yukicommons.nms.event.INMSPacketListener;
import moe.protasis.yukicommons.nms.event.NMSPacketDirection;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.nms.event.NMSPacketHandler;

import java.lang.reflect.Method;

public record RegisteredNMSPacketHandler(
        INMSPacketListener listener,
        IAbstractPlugin plugin,
        NMSPacketHandler handlerAnnotation,
        Class<?> packetClass,
        Method method,
        NMSPacketDirection direction
) {
}
