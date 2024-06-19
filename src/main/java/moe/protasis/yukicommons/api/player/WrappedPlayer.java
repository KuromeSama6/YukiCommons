package moe.protasis.yukicommons.api.player;

import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.api.data.IDatabaseProvider;
import lombok.Getter;
import moe.protasis.yukicommons.api.scheduler.PooledScheduler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a collections of data and methods related to a player.
 * For example, in an economy plugin, the developer may choose to store player
 * data within a class named <code>EconomyPlayer</code> that is a inheritor
 * of <code>WrappedPlayer</code>.
 */
public abstract class WrappedPlayer implements IWrappedPlayer {
    @Getter
    private static final Map<Class<? extends WrappedPlayer>, Map<UUID, WrappedPlayer>> players = new HashMap<>();

    /**
     * The <code>Player</code> that is backing this <code>WrappedPlayer</code> object.
     */
    @Getter
    protected IAbstractPlayer player;
    /**
     * The UUID of the player backing this WrappedPlayer object.
     */
    @Getter
    protected UUID uuid;
    /**
     * Whether this WrappedPlayer's data has been fully loaded (i.e. from a database).
     */
    @Getter
    protected boolean dataReady;
    /**
     * The scheduler that this WrappedPlayer may use.
     */
    @Getter
    protected final PooledScheduler scheduler = new PooledScheduler(GetPlugin().GetScheduler());
    /**
     * A map of all the components this player has. Note that each player
     * may only have one(1) component of each type.
     * @see PlayerComponent
     */
    @Getter
    protected final Map<Class<?>, PlayerComponent<?>> components = new HashMap<>();

    public WrappedPlayer(IAbstractPlayer player) {
        this.player = player;
        uuid = player.GetUuid();

        if (!players.containsKey(getClass())) players.put(getClass(), new HashMap<>());
        players.get(getClass()).put(uuid, this);

        GetPlugin().GetScheduler().RunAsync(() -> {
            dataReady = LoadData();
        });
    }

    /**
     * Gets a component of this player based on a class.
     * @param clazz Class of the component.
     * @return The component, or <code>null</code> if not found.
     * @param <T> The type of the target component.
     * @see PlayerComponent
     */
    public <T extends PlayerComponent<?>> T GetComponent(Class<T> clazz) {
        return (T)components.get(clazz);
    }

    /**
     * Adds a component to this player. Upon adding, the <code>LoadData</code> method of the component
     * added is called immediately and <b>asynchronously</b>.
     * @param component The component.
     * @see PlayerComponent
     */
    protected void AddComponent(PlayerComponent<?> component) {
        components.put(component.getClass(), component);
        GetPlugin().GetScheduler().RunAsync(() -> {
            try {
                component.LoadData();
            } catch (Exception e) {
                GetPlugin().GetLogger().severe(String.format("Error loading component %s for player %s:", component.getClass(), player.GetName()));
                e.printStackTrace();
            }
        });
    }

    protected abstract boolean LoadData();

    /**
     * @return The plugin class from which this WrappedPlayer originated.
     */
    protected abstract IAbstractPlugin GetPlugin();

    /**
     * Saves this WrappedPlayer's data. Fails silently if <code>dataReady</code> is false.
     * The behavior of this method should be <b>blocking</b>.
     */
    public abstract void Save();

    /**
     * @return The <code>IDatabaseProvider</code> that this WrappedPlayer shall use
     * when performing database-related operations.
     * @see IDatabaseProvider
     */
    protected abstract IDatabaseProvider GetDatabaseProvider();

    /**
     * Destroys this WrappedPlayer object, saving it and all of its component first,
     * then removes it from the player list.
     */
    public void Destroy() {
        if (dataReady) {
            GetPlugin().GetScheduler().RunAsync(() -> {
                Save();
                for (PlayerComponent<?> component : components.values()) {
                    try {
                        component.Save();
                    } catch (Exception e) {
                        GetPlugin().GetLogger().severe(String.format("Error saving component %s for player %s:", component.getClass(), player.GetName()));
                        e.printStackTrace();
                    }
                }
            });
        }
        players.get(getClass()).remove(uuid);
    }

    public static <T extends IWrappedPlayer> T GetPlayer(UUID uuid, Class<T> clazz) {
        if (!players.containsKey(clazz)) return null;
        return (T)players.get(clazz).get(uuid);
    }
    public static <T extends IWrappedPlayer> T GetPlayer(Object player, Class<T> clazz) {
        if (player == null) return null;
        IAbstractPlayer abstractPlayer = IAdapter.Get().Adapt(player);
        if (abstractPlayer == null) return null;
        return GetPlayer(abstractPlayer.GetUuid(), clazz);
    }

    public static <T extends WrappedPlayer> List<T> GetPlayers(Class<T> clazz) {
        return players.get(clazz).values().stream()
                .map(c -> (T)c)
                .collect(Collectors.toList());
    }
}
