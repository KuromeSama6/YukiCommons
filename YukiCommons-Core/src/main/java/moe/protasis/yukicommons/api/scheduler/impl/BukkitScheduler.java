package moe.protasis.yukicommons.api.scheduler.impl;

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
}
