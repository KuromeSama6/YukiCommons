package moe.protasis.yukicommons.api.nms.event;

import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import lombok.Getter;
import lombok.ToString;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.api.scheduler.IAbstractScheduler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class RegisteredNMSPacketHandler {
    private static final Map<String, PacketTypeCommon> cache = new HashMap<>();
    private final INMSPacketListener listener;
    private final IAbstractPlugin plugin;
    private final NMSPacketHandler handlerAnnotation;
    private final Class<?> packetClass;
    private final Method method;
    private final Constructor<?> wrapperConstructor;

    public RegisteredNMSPacketHandler(INMSPacketListener listener, IAbstractPlugin plugin, NMSPacketHandler handlerAnnotation, Class<?> packetClass, Method method) {
        this.listener = listener;
        this.plugin = plugin;
        this.handlerAnnotation = handlerAnnotation;
        this.packetClass = packetClass;
        this.method = method;

        wrapperConstructor = Arrays.stream(packetClass.getConstructors())
                .filter(c -> c.getParameterCount() == 1)
                .filter(c -> ProtocolPacketEvent.class.isAssignableFrom(c.getParameterTypes()[0]))
                .findFirst()
                .orElseThrow();
    }

    public NMSPacketDirection GetDirection() {
        return handlerAnnotation.direction();
    }

    public PacketTypeCommon GetPacketType() {
        if (cache.containsKey(handlerAnnotation.type())) {
            return cache.get(handlerAnnotation.type());
        }

        var str = handlerAnnotation.type();
        var args = str.split("\\.");
        if (args.length != 3)
            throw new IllegalArgumentException("Invalid packet type: %s. Expected format: <type>.<direction>.<packet>".formatted(str));

        try {
            var clazz = Class.forName("com.github.retrooper.packetevents.protocol.packettype.PacketType$%s$%s".formatted(args[0], args[1]));
            if (!clazz.isEnum() || !PacketTypeCommon.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Invalid packet type: %s. Expected enum and PacketTypeCommon.".formatted(str));
            }

            for (var constant : clazz.getEnumConstants()) {
                if (constant.toString().equals(args[2])) {
                    cache.put(str, (PacketTypeCommon) constant);
                    return (PacketTypeCommon) constant;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new IllegalArgumentException("Invalid packet type: %s. Expected format: <type>.<direction>.<packet>".formatted(str));
    }

    public void Call(ProtocolPacketEvent event, boolean async, IAbstractScheduler scheduler) {
        try {
            var wrapper = wrapperConstructor.newInstance(event);
            if (async) method.invoke(listener, wrapper);
            else {
                scheduler.CallOnMainThread(() -> {
                    try {
                        method.invoke(listener, wrapper);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

        } catch (Exception e) {
            plugin.GetLogger().severe("Failed to call packet handler %s".formatted(this));
            e.printStackTrace();
        }
    }
}
