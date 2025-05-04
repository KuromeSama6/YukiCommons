package moe.protasis.yukicommons.bukkit.impl.scheduler;

import lombok.AllArgsConstructor;
import moe.protasis.yukicommons.api.scheduler.IAbstractScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@AllArgsConstructor
public class BukkitScheduler implements IAbstractScheduler {
    private final JavaPlugin pl;

    @Override
    public int ScheduleSyncDelayedTask(Runnable func, long delay) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(pl, func, delay);
    }

    @Override
    public int ScheduleSyncRepeatingTask(Runnable func, long delay, long interval) {
//        System.out.println("scheduler: %s, func: %s, delay: %s, interval: %s".formatted(pl.getName(), func, delay, interval));
//        new Exception("Scheduler call stack trace").printStackTrace();
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, func, delay, interval);
    }

    @Override
    public void CancelTask(int handle) {
        Bukkit.getScheduler().cancelTask(handle);
    }

    @Override
    public void RunAsync(Runnable func) {
        Bukkit.getScheduler().runTaskAsynchronously(pl, func);
    }

    @Override
    public void CallOnMainThread(Runnable func) {
        Bukkit.getScheduler().callSyncMethod(pl, () -> {
            func.run();
            return null;
        });
    }
}
