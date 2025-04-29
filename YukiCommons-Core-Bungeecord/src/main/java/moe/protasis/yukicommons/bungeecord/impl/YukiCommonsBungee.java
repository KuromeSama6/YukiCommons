package moe.protasis.yukicommons.bungeecord.impl;

import lombok.Getter;
import moe.protasis.yukicommons.api.IYukiCommons;
import moe.protasis.yukicommons.api.adapter.IAdaptor;
import moe.protasis.yukicommons.api.player.PendingPlayerWrapper;
import moe.protasis.yukicommons.bungeecord.impl.adapter.BungeecordAdaptor;
import moe.protasis.yukicommons.bungeecord.impl.command.BungeecordCommandProvider;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.bungeecord.impl.player.BungeecordPlayerWrapper;
import moe.protasis.yukicommons.util.YukiCommonsApi;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class YukiCommonsBungee extends Plugin implements Listener, IYukiCommons {
    @Getter
    private static YukiCommonsBungee instance;
    @Getter
    private final List<AutoPlayerLoadData> autoPlayerLoadData = new ArrayList<>();
    @Getter
    private IAdaptor adaptor;

    @Override
    public void onEnable() {
        instance = this;
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        adaptor = new BungeecordAdaptor();
        YukiCommonsApi.SetCurrent(new YukiCommonsApiBungeecord());

        new BungeecordCommandProvider();

        ProxyServer.getInstance().getScheduler().schedule(this, this::OnTick, 0, 1000 / 20, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ProxyServer.getInstance().getPlayers().forEach(this::DestroyPlayer);
    }

    public void OnTick() {
        WrappedPlayer.GetAllPlayers().forEach(WrappedPlayer::Update);
    }

    @EventHandler
    public void OnPlayerLogin(LoginEvent e) {
        // auto registers
        for (AutoPlayerLoadData data : autoPlayerLoadData) {
            Class<? extends WrappedPlayer> clazz = data.getPlayerClass();
            try {
                WrappedPlayer player = clazz
                        .getDeclaredConstructor(IAbstractPlayer.class)
                        .newInstance(new PendingPlayerWrapper(e.getConnection().getUniqueId(), e.getConnection().getName()));
                player.LoadAll();

            } catch (LoginDeniedException ex) {
                YukiCommonsBungee.getInstance().getLogger().warning(String.format("Login was denied by %s: %s", clazz, ex.getMessage()));
                e.setCancelled(true);
                e.setCancelReason(new TextComponent(ex.getMessage()));
                WrappedPlayer.DestroyAll(e.getConnection().getUniqueId());
                return;

            } catch (Exception ex) {
                getLogger().severe(String.format("An error occured while instantiating WrappedPlayer class %s:", clazz.getName()));
                ex.printStackTrace();
                e.setCancelled(true);
                e.setCancelReason(new TextComponent("§cThere was an error while loading your data.\nIf you are the server owner, please check the console for more information."));
                WrappedPlayer.DestroyAll(e.getConnection().getUniqueId());
                return;
            }
        }

        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            WrappedPlayer player = map.get(e.getConnection().getUniqueId());
            if (player != null) {
                player.OnPostInit();
            }
        }
    }

    @EventHandler
    public void OnPlayerPostLogin(PostLoginEvent e) {
        // auto registers
        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            WrappedPlayer player = map.get(e.getPlayer().getUniqueId());
            if (player != null) {
                player.FinalizeConnection(new BungeecordPlayerWrapper(e.getPlayer()));
                player.OnJoin();
            }
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerDisconnectEvent e) {
        DestroyPlayer(e.getPlayer());
    }

    private void DestroyPlayer(ProxiedPlayer p) {
        ProxyServer.getInstance().getScheduler().runAsync(this, () -> {
            for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
                WrappedPlayer player = map.get(p.getUniqueId());
                if (player != null) {
                    try {
                        player.SaveAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            WrappedPlayer.DestroyAll(p.getUniqueId());
        });
    }

    @Override
    public Logger GetLogger() {
        return getLogger();
    }
}
