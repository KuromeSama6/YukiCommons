package moe.protasis.yukicommons.api.adapter;

import moe.protasis.yukicommons.YukiCommons;
import moe.protasis.yukicommons.YukiCommonsBungee;
import moe.protasis.yukicommons.api.adapter.impl.BukkitAdapter;
import moe.protasis.yukicommons.api.adapter.impl.BungeecordAdapter;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;

public interface IAdapter {
    IAbstractPlayer Adapt(Object obj);

    static IAdapter Get() {
        if (YukiCommons.getInstance() != null) return new BukkitAdapter();
        else if (YukiCommonsBungee.getInstance() != null) return new BungeecordAdapter();
        else throw new IllegalStateException("Could not get adaptor.");
    }
}
