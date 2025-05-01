package moe.protasis.yukicommons.bukkit.impl;

import lombok.Getter;
import moe.protasis.yukicommons.api.IYukiCommons;
import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.nms.event.IPacketEventPacketListener;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.api.scheduler.IAbstractScheduler;
import moe.protasis.yukicommons.bukkit.impl.scheduler.BukkitScheduler;
import moe.protasis.yukicommons.bukkit.impl.adapter.BukkitAdaptor;
import moe.protasis.yukicommons.bukkit.impl.command.BukkitCommandProvider;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.impl.nms.event.NOPPacketEventPacketListener;
import moe.protasis.yukicommons.bukkit.impl.player.BukkitPlayerWrapper;
import moe.protasis.yukicommons.api.player.PendingPlayerWrapper;
import moe.protasis.yukicommons.api.nms.IVersionAdaptor;
import moe.protasis.yukicommons.bukkit.impl.nms.event.BukkitPacketEventPacketListener;
import moe.protasis.yukicommons.util.Util;
import moe.protasis.yukicommons.util.YukiCommonsApi;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class YukiCommonsBukkit extends JavaPlugin implements Listener, IYukiCommons, IAbstractPlugin {
    @Getter
    private static YukiCommonsBukkit instance;
    @Getter
    private IVersionAdaptor versionAdaptor;
    @Getter
    private IAdaptor adaptor;
    @Getter
    private final List<AutoPlayerLoadData> autoPlayerLoadData = new ArrayList<>();
    @Getter
    private boolean packetEventsEnabled;
    @Getter
    private IPacketEventPacketListener nmsPacketListener;
    private IAbstractScheduler scheduler;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);

        versionAdaptor = IVersionAdaptor.Get();
        adaptor = new BukkitAdaptor();

        getLogger().info("Using version adapter: %s".formatted(versionAdaptor));

        scheduler = new BukkitScheduler(this);

        YukiCommonsApi.SetCurrent(new YukiCommonsApiBukkit());

        new BukkitCommandProvider();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::OnTick, 0, 1);

        packetEventsEnabled = Bukkit.getPluginManager().isPluginEnabled("PacketEvents");
        if (!packetEventsEnabled) {
            getLogger().warning("PacketEvents is not enabled and/or not installed. NMS features may not work.");
        }

        nmsPacketListener = packetEventsEnabled ? new BukkitPacketEventPacketListener() : new NOPPacketEventPacketListener();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getOnlinePlayers().forEach(this::DestroyPlayer);
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
        for (AutoPlayerLoadData data : autoPlayerLoadData) {
            Class<? extends WrappedPlayer> clazz = data.getPlayerClass();

            try {
                synchronized (WrappedPlayer.getPlayers()) {
                    WrappedPlayer pl = clazz
                            .getDeclaredConstructor(IAbstractPlayer.class)
                            .newInstance(new PendingPlayerWrapper(e.getUniqueId(), e.getName()));
                    pl.LoadAll();
                    e.allow();
                }

            } catch (LoginDeniedException ex) {
                getLogger().warning(String.format("Login was denied by %s: %s", clazz, ex.getMessage()));
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ex.getMessage());
                WrappedPlayer.DestroyAll(e.getUniqueId());
                return;

            } catch (Exception ex) {
                getLogger().severe(String.format("An error occured while instantiating WrappedPlayer class %s:", clazz.getName()));
                ex.printStackTrace();
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cThere was an error while loading your data.\nIf you are the server owner, please check the console for more information.");
                return;
            }
        }

        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            WrappedPlayer player = map.get(e.getUniqueId());
            if (player != null) {
                player.OnPostInit();
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

    @EventHandler(priority = EventPriority.LOWEST)
    private void OnPlayerJoin(PlayerJoinEvent e) {
//        System.out.println(String.format("onjoin call started: %s", System.currentTimeMillis()));
        WrappedPlayer.getPlayers().values().forEach(map -> {
            WrappedPlayer player = map.get(e.getPlayer().getUniqueId());
            if (player != null) {
                Util.SafeCall(player::OnReady);
                Util.SafeCall(player::OnJoin);
            }
        });
    }

    @EventHandler
    private void OnPlayerQuit(PlayerQuitEvent e) {
        DestroyPlayer(e.getPlayer());
    }

    private void DestroyPlayer(Player p) {
        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            WrappedPlayer player = map.get(p.getUniqueId());
            if (player != null) {
                Util.SafeCall(player::OnLogout);
            }
        }
        CompletableFuture.runAsync(() -> {
            WrappedPlayer.getPlayers().values().forEach(map -> {
                WrappedPlayer player = map.get(p.getUniqueId());
                if (player != null) {
                    try {
                        player.SaveAll();
                    } catch (Exception e) {
                        getLogger().severe("An error occurred while saving player data:");
                        e.printStackTrace();
                    }
                }
            });
            WrappedPlayer.DestroyAll(p.getUniqueId());
        });
    }

    @Override
    public IAbstractScheduler GetScheduler() {
        return scheduler;
    }

    @Override
    public Logger GetLogger() {
        return getLogger();
    }

    @Override
    public ClassLoader GetClassLoader() {
        return getClassLoader();
    }
}
