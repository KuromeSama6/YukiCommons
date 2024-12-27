package moe.protasis.yukicommons.api.player;

import lombok.var;
import moe.protasis.yukicommons.api.adapter.IAdapter;
import moe.protasis.yukicommons.api.player.impl.PendingPlayerWrapper;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.api.data.IDatabaseProvider;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import lombok.Getter;
import moe.protasis.yukicommons.api.scheduler.PooledScheduler;

import java.lang.reflect.InvocationTargetException;
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
     * The <code>Player</code> that is backing this <code>WrappedPlayer</code>
     * object.
     */
    @Getter
    protected IAbstractPlayer player;
    /**
     * The UUID of the player backing this WrappedPlayer object.
     */
    @Getter
    protected UUID uuid;
    /**
     * Whether this WrappedPlayer's data has been fully loaded (i.e. from a
     * database).
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
     * 
     * @see PlayerComponent
     */
    @Getter
    protected final Map<Class<?>, PlayerComponent<?>> components = new HashMap<>();

    public WrappedPlayer(IAbstractPlayer player) {
        this.player = player;
        uuid = player.GetUuid();

        if (!players.containsKey(getClass()))
            players.put(getClass(), new HashMap<>());
        players.get(getClass()).put(uuid, this);
    }

    /**
     * Gets a component of this player based on a class.
     * 
     * @param clazz Class of the component.
     * @return The component, or <code>null</code> if not found.
     * @param <T> The type of the target component.
     * @see PlayerComponent
     */
    public <T extends PlayerComponent<?>> T GetComponent(Class<T> clazz) {
        return (T) components.get(clazz);
    }

    public <T extends PlayerComponent<?>> T GetOrCreateComponent(Class<T> clazz) {
        var component = GetComponent(clazz);
        if (component != null) return component;

        try {
            var obj = clazz.getDeclaredConstructor(getClass())
                    .newInstance(this);
            var ret = clazz.cast(obj);
            AddComponent(ret);
            return ret;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds a component to this player. Upon adding, the <code>LoadData</code>
     * method of the component
     * added is called immediately and <b>asynchronously</b>.
     * 
     * @param component The component.
     * @see PlayerComponent
     */
    protected void AddComponent(PlayerComponent<?> component) {
        components.put(component.getClass(), component);

        switch (component.GetLoadType()) {
            case BLOCKING:
                BlockingLoadComponent(component);
                break;
            case PRIORITY:
            case REGULAR:
                GetPlugin().GetScheduler().RunAsync(() -> BlockingLoadComponent(component));
                break;
        }
    }

    private void BlockingLoadComponent(PlayerComponent<?> component) {
        try {
            component.LoadData();
        } catch (Exception e) {
            GetPlugin().GetLogger().severe(String.format("Error loading component %s for player %s:",
                    component.getClass(), player.GetName()));
            e.printStackTrace();
        }
    }

    public void BlockingLoadData() {
        if (dataReady)
            throw new IllegalStateException();
        switch (GetLoadType()) {
            case BLOCKING:
                dataReady = LoadData();
                break;
            case PRIORITY:
            case REGULAR:
                GetPlugin().GetScheduler().RunAsync(() -> dataReady = LoadData());
                break;
        }
    }

    /**
     * Called when a player is attempting login. Login can be denied by overriding
     * this method and throwing <code>LoginDeniedException</code>.
     * 
     * @throws LoginDeniedException When the login attempt is denied by this
     *                              WrappedPlayer object.
     */
    public void AttemptLogin() throws LoginDeniedException {
    }

    /**
     * Loads the data for this WrappedPlayer object. It is not guaranteed that operations to the player object are safe, and thus such operations are not recommended.
     * @return Whether the data was loaded successfully. Note that login rejections should be handled by the <code>AttemptLogin</code> method.
     */
    protected boolean LoadData() {
        return true;
    }

    /**
     * @return Whether the <code>LoadData</code> method of this class should
     *         be called asynchronously. Defaults to <code>true</code>.
     */
    protected EntityLoadType GetLoadType() {
        return EntityLoadType.REGULAR;
    }

    /**
     * @return The plugin class from which this WrappedPlayer originated.
     */
    protected abstract IAbstractPlugin GetPlugin();

    /**
     * Saves this WrappedPlayer's data. The implementation should silently if <code>dataReady</code> is
     * false.
     * The behavior of this method should be <b>blocking</b>.
     */
    public void Save() {}

    /**
     * @return The <code>IDatabaseProvider</code> that this WrappedPlayer shall use
     *         when performing database-related operations.
     * @see IDatabaseProvider
     */
    protected abstract IDatabaseProvider GetDatabaseProvider();

    /**
     * Destroys this WrappedPlayer object, saving it and all of its component first,
     * then removes it from the player list.
     */
    public void Destroy() {
        Destroy(true);
    }

    public void Destroy(boolean save) {
        if (dataReady && save) {
            GetPlugin().GetScheduler().RunAsync(() -> {
                Save();
                for (PlayerComponent<?> component : components.values()) {
                    try {
                        component.Save();
                    } catch (Exception e) {
                        GetPlugin().GetLogger().severe(String.format("Error saving component %s for player %s:",
                                component.getClass(), player.GetName()));
                        e.printStackTrace();
                    }
                }
            });
        }
        players.get(getClass()).remove(uuid);
    }

    public void FinalizeConnection(IAbstractPlayer player) {
        if (!(this.player instanceof PendingPlayerWrapper))
            throw new IllegalStateException();
        this.player = player;
    }

    /**
     * Utility method to run a <code>Runnable</code> on the plugin's main thread.
     * 
     * @param runnable The runnable.
     */
    protected final void RunOnMain(Runnable runnable) {
        GetPlugin().GetScheduler().ScheduleSyncDelayedTask(runnable, 0);
    }

    /**
     * Called every tick.
     */
    public void Update() {
        for (var component : components.values()) {
            component.Update();
        }
    }

    /**
     * Called after the player has joined the server, and operations to the player object are safe.
     * On Bukkit, this is called after the <code>PlayerJoinEvent</code> is fired.
     * On BungeeCord, this is called after the <code>PostLoginEvent</code> is fired and <code>FinalizeConnection</code> is called on the wrapped player.
     */
    public void OnJoin() {}

    /**
     * Called after ALL WrappedPlayer instances registered by every plugin have been initialized.
     */
    public void OnPostInit() {}

    public <T extends WrappedPlayer> T GetPlayer(Class<T> clazz) {
        return GetPlayer(uuid, clazz);
    }

    public static <T extends IWrappedPlayer> T GetPlayer(UUID uuid, Class<T> clazz) {
        if (!players.containsKey(clazz))
            return null;
        return (T) players.get(clazz).get(uuid);
    }

    public static <T extends IWrappedPlayer> T GetPlayer(Object player, Class<T> clazz) {
        if (player == null)
            return null;
        IAbstractPlayer abstractPlayer = IAdapter.Get().AdaptToPlayer(player);
        if (abstractPlayer == null)
            return null;
        return GetPlayer(abstractPlayer.GetUuid(), clazz);
    }

    public static <T extends WrappedPlayer> List<T> GetAllPlayers() {
        return players.values().stream()
                .flatMap(c -> c.values().stream())
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    public static <T extends WrappedPlayer> List<T> GetAllPlayers(Class<T> clazz) {
        return players.get(clazz).values().stream()
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    public static void DestroyAll(UUID uuid) {
        for (Map<UUID, WrappedPlayer> map : players.values()) {
            if (map.containsKey(uuid)) {
                map.get(uuid).Destroy();
            }
        }
    }
}
