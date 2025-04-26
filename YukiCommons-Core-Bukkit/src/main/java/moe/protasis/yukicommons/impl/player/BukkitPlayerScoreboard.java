package moe.protasis.yukicommons.impl.player;

import fr.mrmicky.fastboard.FastBoard;
import moe.protasis.yukicommons.YukiCommonsBukkit;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.display.IScoreboardProvider;
import moe.protasis.yukicommons.api.misc.IDestroyable;
import moe.protasis.yukicommons.api.scheduler.PooledScheduler;
import org.bukkit.ChatColor;

import java.util.Stack;

public class BukkitPlayerScoreboard implements IScoreboard, IDestroyable {
    private final BukkitPlayerWrapper player;
    private final PooledScheduler scheduler;
    private int updateCounter;
    private FastBoard board;
    private final Stack<IScoreboardProvider> providers = new Stack<>();

    public BukkitPlayerScoreboard(BukkitPlayerWrapper player) {
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

        var visible = provider != null && provider.IsVisible();
        if (visible && board == null) {
            board = new FastBoard(player.getPlayer());
        } else if (!visible && board != null) {
            board.delete();
            board = null;
        }

        if (provider == null || board == null) return;

        board.updateTitle(ChatColor.translateAlternateColorCodes('&', provider.GetTitle()));
        board.updateLines(provider.GetLines().stream().map(line -> ChatColor.translateAlternateColorCodes('&', line)).toList());
    }

    @Override
    public void Destroy() {
        if (board != null) board.delete();
        scheduler.Free();
    }

    @Override
    public IScoreboardProvider GetActiveProvider() {
        return providers.isEmpty() ? null : providers.peek();
    }

    @Override
    public void AddProvider(IScoreboardProvider provider) {
        if (provider == null) return;
        providers.remove(provider);
        providers.push(provider);
    }

    @Override
    public void RemoveProvider(IScoreboardProvider provider) {
        providers.remove(provider);
    }
}
