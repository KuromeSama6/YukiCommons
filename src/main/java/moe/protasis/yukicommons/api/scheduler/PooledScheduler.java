package moe.protasis.yukicommons.api.scheduler;

import lombok.Getter;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;

import java.util.ArrayList;
import java.util.List;

public class PooledScheduler {
    private final IAbstractScheduler scheduler;
    @Getter private final List<Integer> handles = new ArrayList<>();

    public PooledScheduler(IAbstractScheduler scheduler) {
        this.scheduler = scheduler;
    }
    public PooledScheduler(IAbstractPlugin plugin) {
        this(plugin.GetScheduler());
    }

    public int Add(Runnable func, long delay) {
        int handle = scheduler.ScheduleSyncDelayedTask(func, delay);
        handles.add(handle);
        return handle;
    }

    public int AddRepeating(Runnable func, long delay, long interval) {
        int handle = scheduler.ScheduleSyncRepeatingTask(func, delay, interval);
        handles.add(handle);
        return handle;
    }

    public void JoinMain(Runnable func) {
       scheduler.ScheduleSyncDelayedTask(func, 0);
    }

    public void Free() {
        for (int handle : handles) scheduler.CancelTask(handle);
    }

    public void Cancel(int handle) {
        scheduler.CancelTask(handle);
        handles.remove((Object)handle);
    }

    public void RunAsync(Runnable func) {
        scheduler.RunAsync(func);
    }

}
