package moe.protasis.yukicommons.impl.nms.event;

import moe.protasis.yukicommons.api.nms.event.INMSPacketListener;
import moe.protasis.yukicommons.api.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

public class NOPPacketEventPacketListener implements IPacketEventPacketListener {
    @Override
    public void Register(INMSPacketListener listener, IAbstractPlugin plugin) {

    }

    @Override
    public void Unregister(INMSPacketListener listener) {

    }

    @Override
    public void UnregisterAll(IAbstractPlugin plugin) {

    }
}
