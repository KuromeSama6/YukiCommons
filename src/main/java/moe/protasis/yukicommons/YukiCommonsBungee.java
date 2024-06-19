package moe.protasis.yukicommons;

import lombok.Getter;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class YukiCommonsBungee extends Plugin implements Listener {
    @Getter
    private static YukiCommonsBungee instance;
    @Getter
    private final List<Class<? extends WrappedPlayer>> autoRegisters = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @EventHandler
    public void OnPlayerPostLogin(PostLoginEvent e) {
        // auto registers
        for (Class<? extends WrappedPlayer> clazz : YukiCommons.getInstance().getAutoRegisters()) {
            try {
//                System.out.println(String.format("new class from auto register %s", clazz));
                clazz
                        .getDeclaredConstructor(IAbstractPlayer.class)
                        .newInstance(IAdapter.Get().Adapt(e.getPlayer()));
            } catch (Exception ex) {
                getLogger().severe(String.format("An error occured while instantiating WrappedPlayer class %s:", clazz.getName()));
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerDisconnectEvent e) {
        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            if (map.containsKey(e.getPlayer().getUniqueId()))
                map.get(e.getPlayer().getUniqueId()).Destroy();
        }
    }
}
