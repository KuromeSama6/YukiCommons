package moe.protasis.yukicommons;

import lombok.Getter;
import moe.protasis.yukicommons.api.IYukiCommons;
import moe.protasis.yukicommons.impl.YukiCommonsApiBukkit;
import moe.protasis.yukicommons.impl.adapter.BukkitAdapter;
import moe.protasis.yukicommons.impl.command.BukkitCommandProvider;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.impl.player.BukkitPlayerWrapper;
import moe.protasis.yukicommons.api.player.PendingPlayerWrapper;
import moe.protasis.yukicommons.nms.IVersionAdaptor;
import moe.protasis.yukicommons.impl.nms.event.PacketEventPacketListenerSpigot;
import moe.protasis.yukicommons.util.Singletons;
import moe.protasis.yukicommons.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class YukiCommonsBukkit extends JavaPlugin implements Listener, IYukiCommons {
    @Getter
    private static YukiCommonsBukkit instance;
    @Getter
    private IVersionAdaptor versionAdaptor;
    @Getter
    private final List<AutoPlayerLoadData> autoPlayerLoadData = new ArrayList<>();
    @Getter
    private boolean packetEventsEnabled;
    @Getter
    private PacketEventPacketListenerSpigot nmsPacketListener;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);
        versionAdaptor = IVersionAdaptor.Get();
        getLogger().info("Using version adapter: %s".formatted(versionAdaptor));

        Singletons.Set(new BukkitAdapter());
        Singletons.Set(versionAdaptor);
        Singletons.Set(new YukiCommonsApiBukkit());

        new BukkitCommandProvider();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::OnTick, 0, 1);

        packetEventsEnabled = Bukkit.getPluginManager().isPluginEnabled("PacketEvents");
        if (!packetEventsEnabled) {
            getLogger().warning("PacketEvents is not enabled and/or not installed. NMS features may not work.");
        }

        nmsPacketListener = new PacketEventPacketListenerSpigot();
    }

    public void OnTick() {
        WrappedPlayer.GetAllPlayers().forEach(c -> {
            if (c.GetPlayer().GetBukkitPlayer() != null) {
                Util.SafeCall(c::Update);
            }
        });
    }


    @EventHandler
    private void OnPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
//        System.out.println(String.format("login started: %s", System.currentTimeMillis()));
//        System.out.println(String.format("login next: %s", System.currentTimeMillis()));

        // auto registers
//        System.out.println("player pre login");
        for (AutoPlayerLoadData data : autoPlayerLoadData) {
            Class<? extends WrappedPlayer> clazz = data.getPlayerClass();

            try {
//                System.out.println(String.format("new class from auto register %s", clazz));
                synchronized (WrappedPlayer.getPlayers()) {
                    WrappedPlayer pl = clazz
                            .getDeclaredConstructor(IAbstractPlayer.class)
                            .newInstance(new PendingPlayerWrapper(e.getUniqueId()));
                    pl.BlockingLoadData();
                    pl.AttemptLogin();
                    e.allow();
//                    System.out.println(String.format("login done: %s, %s", System.currentTimeMillis(), WrappedPlayer.GetPlayer(e.getUniqueId(), clazz)));
                }

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

        WrappedPlayer.GetAllPlayers().forEach(WrappedPlayer::OnPostInit);
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

    @EventHandler(priority = EventPriority.LOWEST)
    private void OnPlayerJoin(PlayerJoinEvent e) {
//        System.out.println(String.format("onjoin call started: %s", System.currentTimeMillis()));
        WrappedPlayer.getPlayers().values().forEach(map -> {
            WrappedPlayer player = map.get(e.getPlayer().getUniqueId());
            if (player != null) {
                Util.SafeCall(player::OnJoin);
            }
        });
    }

    @EventHandler
    private void OnPlayerQuit(PlayerQuitEvent e) {
        WrappedPlayer.DestroyAll(e.getPlayer().getUniqueId());
    }
}
