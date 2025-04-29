package moe.protasis.yukicommons.bukkit.impl.player;

import fr.mrmicky.fastboard.FastBoard;
import moe.protasis.yukicommons.api.display.IExperienceBar;
import moe.protasis.yukicommons.api.display.IExperienceBarProvider;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.display.IScoreboardProvider;
import moe.protasis.yukicommons.api.misc.IDestroyable;
import moe.protasis.yukicommons.api.scheduler.PooledScheduler;
import moe.protasis.yukicommons.bukkit.impl.YukiCommonsBukkit;
import org.bukkit.ChatColor;

import java.util.Stack;

public class BukkitPlayerExperienceBar implements IExperienceBar, IDestroyable {
    private final BukkitPlayerWrapper player;
    private final PooledScheduler scheduler;
    private int updateCounter;
    private final Stack<IExperienceBarProvider> providers = new Stack<>();

    public BukkitPlayerExperienceBar(BukkitPlayerWrapper player) {
        this.player = player;
        scheduler = new PooledScheduler(YukiCommonsBukkit.getInstance());

        scheduler.AddRepeating(this::Tick, 0, 1);
    }

    private void Tick() {
        var provider = GetActiveProvider();

        if (provider != null) {
            updateCounter++;
            if (updateCounter >= provider.GetUpdateInterval()) {
                updateCounter = 0;
                Update();
            }

        } else {
            updateCounter = 0;
        }
    }

    @Override
    public void Update() {
        var provider = GetActiveProvider();
        var bp = player.getPlayer();

        if (provider == null) {
            bp.setExp(0);
            bp.setLevel(0);
            return;
        }

        bp.setExp((float)provider.GetProgress());
        bp.setLevel(provider.GetLevel());
    }

    @Override
    public void Destroy() {
        scheduler.Free();
    }

    @Override
    public IExperienceBarProvider GetActiveProvider() {
        return providers.isEmpty() ? null : providers.peek();
    }

    @Override
    public void AddProvider(IExperienceBarProvider provider) {
        if (provider == null) return;
        providers.remove(provider);
        providers.push(provider);
    }

    @Override
    public void RemoveProvider(IExperienceBarProvider provider) {
        providers.remove(provider);
    }

    @Override
    public void RemoveAllProviders() {
        providers.clear();
    }
}
