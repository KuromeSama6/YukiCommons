package moe.protasis.yukicommons.api.nms.event;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;

public class PacketEventPacketListenerImpl implements IPacketEventPacketListener, PacketListener {
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        var user = event.getUser();
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {

    }
}
