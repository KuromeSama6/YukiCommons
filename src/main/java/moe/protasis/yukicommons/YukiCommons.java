package moe.protasis.yukicommons;

import lombok.Getter;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.command.CommandProvider;
import moe.protasis.yukicommons.api.command.impl.BukkitCommandProvider;
import moe.protasis.yukicommons.api.player.IAbstractPlayer;
import moe.protasis.yukicommons.api.player.WrappedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, this);

        new BukkitCommandProvider();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void OnPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();

        // auto registers
        for (Class<? extends WrappedPlayer> clazz : YukiCommons.getInstance().getAutoRegisters()) {
            try {
//                System.out.println(String.format("new class from auto register %s", clazz));
                clazz
                        .getDeclaredConstructor(IAbstractPlayer.class)
                        .newInstance(IAdapter.Get().AdaptToPlayer(player));
            } catch (Exception ex) {
                getLogger().severe(String.format("An error occured while instantiating WrappedPlayer class %s:", clazz.getName()));
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    private void OnPlayerQuit(PlayerQuitEvent e) {
        for (Map<UUID, WrappedPlayer> map : WrappedPlayer.getPlayers().values()) {
            if (map.containsKey(e.getPlayer().getUniqueId()))
                map.get(e.getPlayer().getUniqueId()).Destroy();
        }
    }
}
