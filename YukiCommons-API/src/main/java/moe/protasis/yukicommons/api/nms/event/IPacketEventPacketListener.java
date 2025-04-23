package moe.protasis.yukicommons.api.nms.event;

import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

public interface IPacketEventPacketListener {
    void Register(INMSPacketListener listener, IAbstractPlugin plugin);
}
