package moe.protasis.yukicommons.velocity.impl;

import com.google.inject.Inject;
import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import moe.protasis.yukicommons.api.IYukiCommons;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import moe.protasis.yukicommons.api.player.AutoPlayerLoadData;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import moe.protasis.yukicommons.velocity.impl.adapter.VelocityAdaptor;
import moe.protasis.yukicommons.velocity.impl.command.VelocityCommandProvider;
import moe.protasis.yukicommons.util.YukiCommonsApi;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Plugin(
        id = "yukicommons", name = "YukiCommons", version = "1.3.0",
        description = "YukiCommons for Velocity", authors = "KuromeSama6"
)
public class YukiCommonsVelocity implements IYukiCommons {
    @Getter
    private static YukiCommonsVelocity instance;
    @Getter
    private final ProxyServer server;
    private final Logger logger;
    @Getter
    private final VelocityAdaptor adaptor;
    private final YukiCommonsApiVelocity api;
    @Getter
    private final List<AutoPlayerLoadData> autoPlayerLoadData = new ArrayList<>();

    @Inject
    public YukiCommonsVelocity(ProxyServer server, Logger logger) {
        instance = this;
        this.server = server;
        this.logger = logger;

        adaptor = new VelocityAdaptor();

        new VelocityCommandProvider();

        api = new YukiCommonsApiVelocity(this);
        YukiCommonsApi.SetCurrent(api);
    }

    @Subscribe
    private void OnProxyInitialization(ProxyInitializeEvent e) {
        logger.info("YukiCommons for Velocity loaded.");

        server.getScheduler().buildTask(this, this::Update)
                .repeat(1000 / 20, TimeUnit.MILLISECONDS)
                .schedule();
    }

    @Subscribe
    private void OnLogin(LoginEvent e) {
        for (AutoPlayerLoadData data : autoPlayerLoadData) {
            Class<? extends WrappedPlayer> clazz = data.getPlayerClass();
            try {
                WrappedPlayer player = clazz
                        .getDeclaredConstructor(IAbstractPlayer.class)
                        .newInstance(adaptor.AdaptToPlayer(e.getPlayer()));
                player.BlockingLoadData();
                player.AttemptLogin();

            } catch (LoginDeniedException ex) {
                logger.warning(String.format("Login was denied by %s: %s", clazz, ex.getMessage()));
                e.setResult(ResultedEvent.ComponentResult.denied(Component.text(ex.getMessage())));
                WrappedPlayer.DestroyAll(e.getPlayer().getUniqueId());
                return;

            } catch (Exception ex) {
                GetLogger().severe(String.format("An error occured while instantiating WrappedPlayer class %s:", clazz.getName()));
                ex.printStackTrace();
            }
        }

        WrappedPlayer.GetAllPlayers().forEach(WrappedPlayer::OnPostInit);
    }

    @Subscribe
    public void OnPlayerPostLogin(PostLoginEvent e) {
        // auto registers
        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            WrappedPlayer player = map.get(e.getPlayer().getUniqueId());
            if (player != null) {
                player.OnJoin();
            }
        }
    }

    @Subscribe
    public void OnPlayerQuit(DisconnectEvent e) {
        WrappedPlayer.DestroyAll(e.getPlayer().getUniqueId());
    }

    private void Update() {
        WrappedPlayer.GetAllPlayers().forEach(WrappedPlayer::Update);
    }

    @Override
    public Logger GetLogger() {
        return logger;
    }
}
