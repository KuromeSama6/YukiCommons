package moe.protasis.yukicommons.impl.nms.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketTypeData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.login.client.WrapperLoginClientCookieResponse;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import moe.protasis.yukicommons.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.nms.event.INMSPacketListener;
import moe.protasis.yukicommons.api.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.nms.event.NMSPacketHandler;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.api.nms.event.RegisteredNMSPacketHandler;
import moe.protasis.yukicommons.util.PacketEventsClassMapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEventPacketListenerSpigot implements IPacketEventPacketListener, PacketListener {
    private final Map<Class<?>, Set<RegisteredNMSPacketHandler>> packetListeners = new ConcurrentHashMap<>();
    private final PacketEventsClassMapper eventsClassMapper;

    public PacketEventPacketListenerSpigot() {
        PacketEvents.getAPI().getEventManager().registerListener(this, PacketListenerPriority.NORMAL);

        eventsClassMapper = new PacketEventsClassMapper(YukiCommonsBukkit.getInstance());
    }

    @Override
    public void Register(INMSPacketListener listener, IAbstractPlugin plugin) {
        for (var method : listener.getClass().getDeclaredMethods()) {
            var args = method.getParameterTypes();
            if (args.length != 1) {
                plugin.GetLogger().warning("Could not register NMS packet handler %s: Expected 1 argument.".formatted(method));
                continue;
            }
            var packetClass = args[0];
            if (!PacketWrapper.class.isAssignableFrom(packetClass)) {
                plugin.GetLogger().warning("Could not register NMS packet handler %s: Expected PacketWrapper<?> argument.".formatted(method));
                continue;
            }

            var handler = method.getAnnotation(NMSPacketHandler.class);
            if (handler == null) continue;
            var direction = handler.direction();

            var packetHandler = new RegisteredNMSPacketHandler(listener, plugin, handler, packetClass, method, direction);
            if (!packetListeners.containsKey(packetClass)) {
                packetListeners.put(packetClass, new HashSet<>());
            }

            var listeners = packetListeners.get(packetClass);
            if (listeners != null) {
                listeners.add(packetHandler);
            }
        }
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        var user = event.getUser();
        var packet = event.getPacketType();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
    }

}
