package moe.protasis.yukicommons.api.nms.event;

import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

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
