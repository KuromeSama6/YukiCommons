package moe.protasis.yukicommons.bungeecord.impl.scheduler;

import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.scheduler.IAbstractScheduler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class BungeecordScheduler implements IAbstractScheduler {
    private final Plugin pl;

    @Override
    public int ScheduleSyncDelayedTask(Runnable func, long delay) {
        return ProxyServer.getInstance().getScheduler().schedule(pl, func, delay, TimeUnit.SECONDS).getId();
    }

    @Override
    public int ScheduleSyncRepeatingTask(Runnable func, long delay, long interval) {
        return ProxyServer.getInstance().getScheduler().schedule(pl, func, delay, interval, TimeUnit.SECONDS).getId();
    }

    @Override
    public void CancelTask(int handle) {
        ProxyServer.getInstance().getScheduler().cancel(handle);
    }

    @Override
    public void RunAsync(Runnable func) {
        ProxyServer.getInstance().getScheduler().runAsync(pl, func);
    }

    @Override
    public void CallOnMainThread(Runnable func) {
        ProxyServer.getInstance().getScheduler().schedule(pl, func, 0, TimeUnit.MILLISECONDS);
    }
}
