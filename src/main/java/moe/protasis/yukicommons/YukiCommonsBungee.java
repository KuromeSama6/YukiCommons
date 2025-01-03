package moe.protasis.yukicommons;

import lombok.Getter;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.impl.BungeecordCommandProvider;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.api.player.impl.BungeecordPlayerWrapper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
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

public class YukiCommonsBungee extends Plugin implements Listener {
    @Getter
    private static YukiCommonsBungee instance;
    @Getter
    private final List<AutoPlayerLoadData> autoPlayerLoadData = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        new BungeecordCommandProvider();

        ProxyServer.getInstance().getScheduler().schedule(this, this::OnTick, 0, 1000 / 20, TimeUnit.MILLISECONDS);
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
                        .newInstance(IAdapter.Get().AdaptToPlayer(e.getConnection()));
                player.BlockingLoadData();
                player.AttemptLogin();

            } catch (LoginDeniedException ex) {
                YukiCommonsBungee.getInstance().getLogger().warning(String.format("Login was denied by %s: %s", clazz, ex.getMessage()));
                e.setCancelled(true);
                e.setCancelReason(new TextComponent(ex.getMessage()));
                WrappedPlayer.DestroyAll(e.getConnection().getUniqueId());
                return;

            } catch (Exception ex) {
                getLogger().severe(String.format("An error occured while instantiating WrappedPlayer class %s:", clazz.getName()));
                ex.printStackTrace();
            }
        }

        WrappedPlayer.GetAllPlayers().forEach(WrappedPlayer::OnPostInit);
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
        WrappedPlayer.DestroyAll(e.getPlayer().getUniqueId());
    }
}
