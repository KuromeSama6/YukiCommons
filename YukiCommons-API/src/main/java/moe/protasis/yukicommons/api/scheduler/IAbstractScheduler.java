package moe.protasis.yukicommons.api.scheduler;

public interface IAbstractScheduler {
    int ScheduleSyncDelayedTask(Runnable func, long delay);
    int ScheduleSyncRepeatingTask(Runnable func, long delay, long interval);
    void CancelTask(int handle);
    void RunAsync(Runnable func);
}
