package moe.protasis.yukicommons.api.adapter;

import moe.protasis.yukicommons.YukiCommons;
import moe.protasis.yukicommons.YukiCommonsBungee;
import moe.protasis.yukicommons.api.adapter.impl.BukkitAdapter;
import moe.protasis.yukicommons.api.adapter.impl.BungeecordAdapter;
import moe.protasis.yukicommons.api.command.IAbstractCommandExecutor;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.util.EnvironmentType;
import moe.protasis.yukicommons.util.Util;

public interface IAdapter {
    IAbstractPlayer AdaptToPlayer(Object obj);
    IAbstractCommandExecutor AdaptToCommandExecutor(Object obj);

    static IAdapter Get() {
        if (Util.GetEnvironment() == EnvironmentType.SPIGOT) return new BukkitAdapter();
        else if (Util.GetEnvironment() == EnvironmentType.PROXY) return new BungeecordAdapter();
        else throw new IllegalStateException("Could not get adaptor.");
    }
}
