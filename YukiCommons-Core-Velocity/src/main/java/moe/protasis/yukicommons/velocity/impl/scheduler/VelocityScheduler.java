package moe.protasis.yukicommons.velocity.impl.scheduler;

import com.velocitypowered.api.scheduler.ScheduledTask;
import moe.protasis.yukicommons.velocity.impl.YukiCommonsVelocity;
import moe.protasis.yukicommons.api.scheduler.IAbstractScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VelocityScheduler implements IAbstractScheduler {
    private final Object plugin;
    private final Map<Integer, ScheduledTask> handles = new HashMap<>();
    private int handleCounter = 0;

    public VelocityScheduler(Object plugin) {
        Objects.requireNonNull(plugin);
        this.plugin = plugin;
    }

    @Override
    public int ScheduleSyncDelayedTask(Runnable func, long delay) {
        var task = YukiCommonsVelocity.getInstance().getServer().getScheduler()
                .buildTask(plugin, func)
                .delay(delay, TimeUnit.MILLISECONDS)
                .schedule();
        int handle = handleCounter++;
        handles.put(handle, task);
        return handle;
    }

    @Override
    public int ScheduleSyncRepeatingTask(Runnable func, long delay, long interval) {
        var task = YukiCommonsVelocity.getInstance().getServer().getScheduler()
                .buildTask(plugin, func)
                .delay(delay, TimeUnit.MILLISECONDS)
                .repeat(interval, TimeUnit.MILLISECONDS)
                .schedule();
        int handle = handleCounter++;
        handles.put(handle, task);
        return handle;
    }

    @Override
    public void CancelTask(int handle) {
        if (!handles.containsKey(handle)) {
            return;
        }

        var task = handles.get(handle);
        if (task != null) {
            task.cancel();
        }
        handles.remove(handle);
    }

    @Override
    public void RunAsync(Runnable func) {
        CallOnMainThread(func);
    }

    @Override
    public void CallOnMainThread(Runnable func) {
        YukiCommonsVelocity.getInstance().getServer().getScheduler()
                .buildTask(plugin, func)
                .schedule();
    }
}
