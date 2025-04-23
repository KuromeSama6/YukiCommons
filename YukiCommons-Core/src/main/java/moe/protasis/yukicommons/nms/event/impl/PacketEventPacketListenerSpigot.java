package moe.protasis.yukicommons.nms.event.impl;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerDisconnect;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientAdvancementTab;
import moe.protasis.yukicommons.nms.event.INMSPacketListener;
import moe.protasis.yukicommons.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.nms.event.NMSPacketHandler;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketEventPacketListenerImpl implements IPacketEventPacketListener, PacketListener {
    private final Map<Class<?>, Set<RegisteredNMSPacketHandler>> packetListeners = new ConcurrentHashMap<>();

    public PacketEventPacketListenerImpl() {

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

        System.out.println(packet.getName());
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
    }

}
