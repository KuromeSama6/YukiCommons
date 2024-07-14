package moe.protasis.yukicommons;

import lombok.Getter;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.impl.BukkitCommandProvider;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.player.impl.BukkitPlayerWrapper;
import moe.protasis.yukicommons.api.player.impl.PendingPlayerWrapper;
import moe.protasis.yukicommons.nms.IVersionAdaptor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class YukiCommons extends JavaPlugin implements Listener {
    @Getter
    private static YukiCommons instance;
    @Getter
    private final List<Class<? extends WrappedPlayer>> autoRegisters = new ArrayList<>();
    @Getter
    private IVersionAdaptor versionAdaptor;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        versionAdaptor = IVersionAdaptor.Get();
        new BukkitCommandProvider();
    }

    @EventHandler
    private void OnPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        // auto registers
        for (Class<? extends WrappedPlayer> clazz : YukiCommons.getInstance().getAutoRegisters()) {
            try {
//                System.out.println(String.format("new class from auto register %s", clazz));
                WrappedPlayer pl = clazz
                        .getDeclaredConstructor(IAbstractPlayer.class)
                        .newInstance(new PendingPlayerWrapper(e.getUniqueId()));
                pl.BlockingLoadData();
                pl.AttemptLogin();

            } catch (LoginDeniedException ex) {
                YukiCommonsBungee.getInstance().getLogger().warning(String.format("Login was denied by %s: %s", clazz, ex.getMessage()));
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ex.getMessage());
                WrappedPlayer.DestroyAll(e.getUniqueId());
                return;

            } catch (Exception ex) {
                getLogger().severe(String.format("An error occured while instantiating WrappedPlayer class %s:", clazz.getName()));
                ex.printStackTrace();
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void OnPlayerLogin(PlayerLoginEvent e) {
        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            WrappedPlayer player = map.get(e.getPlayer().getUniqueId());
            if (player != null) {
                player.FinalizeConnection(new BukkitPlayerWrapper(e.getPlayer()));
            }
        }
    }

    @EventHandler
    private void OnPlayerQuit(PlayerQuitEvent e) {
        WrappedPlayer.DestroyAll(e.getPlayer().getUniqueId());
    }
}
