package moe.protasis.yukicommons.api.player;

import lombok.Setter;
import moe.protasis.yukicommons.api.display.IScoreboard;
import moe.protasis.yukicommons.api.plugin.IAbstractPlugin;
import moe.protasis.yukicommons.api.exception.LoginDeniedException;
import lombok.Getter;
import moe.protasis.yukicommons.api.scheduler.PooledScheduler;
import moe.protasis.yukicommons.util.YukiCommonsApi;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents a collections of data and methods related to a player.
 * For example, in an economy plugin, the developer may choose to store player
 * data within a class named <code>EconomyPlayer</code> that is an inheritor
 * of <code>WrappedPlayer</code>.
 */
public abstract class WrappedPlayer implements IWrappedPlayer {
    @Getter
    private static final Map<Class<? extends WrappedPlayer>, Map<UUID, WrappedPlayer>> players = new ConcurrentHashMap<>();

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
     * Whether the data of this WrappedPlayer is pristine. Players with pristine data are not saved when automatic saving is called. dataPristine will only be true when (a) a player has logged in and his data are not yet been loaded, or (b) the player has logged out and his data are already saved;
     */
    @Getter @Setter
    private boolean dataPristine = true;
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
    protected final Map<Class<?>, PlayerComponent<?>> components = new ConcurrentHashMap<>();

    private boolean dataLoadBegun;

    /**
     * Creates a new WrappedPlayer object.
     * <p>
     * Any player components added in the constructor will be loaded when LoadData is called subsequently.
     * @param player The player backing this WrappedPlayer object.
     */
    public WrappedPlayer(IAbstractPlayer player) {
        this.player = player;
        uuid = player.GetUuid();

        if (!players.containsKey(getClass()))
            players.put(getClass(), new ConcurrentHashMap<>());
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

    public PlayerComponent<?> GetComponentUnsafe(Class<?> clazz) {
        return components.get(clazz);
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
        if (dataLoadBegun) {
            throw new IllegalStateException("Components can only be added in the constructor or before LoadData is called.");
        }

        components.put(component.getClass(), component);
    }

    /**
     * Loads the data for this WrappedPlayer object. This method is automatically called upon login. Throwing an error in this method causes the login attempted to be denied.
     * <p>
     * At the time that this method is called, the underlying IAbstractPlayer has not yet been fully initialized. The safety of any calls to the underlying IAbstractPlayer cannot be guaranteed and will lead to undefined behavior.
     *
     * @throws LoginDeniedException When the login attempt is denied by this WrappedPlayer object. A detailed reason message will be shown instead of the default error message.
     * @throws Exception            When an error occurs while loading data.
     */
    @Override
    public void LoadData() throws Exception {
    }

    /**
     * Gets a list of data components related to this player. This list is used to determine what objects to save when a player logs in or out.
     * <p>
     * By default, this list contains the player instance itself, and all components that are registered to this player.
     * @return A list of data components related to this player.
     */
    public List<IDataComponent> GetDataComponents() {
        var list = new ArrayList<IDataComponent>();
        list.add(this);
        list.addAll(components.values());
        return list;
    }

    /**
     * @return The plugin class from which this WrappedPlayer originated.
     */
    protected abstract IAbstractPlugin GetPlugin();

    /**
     * Saves this WrappedPlayer's data.
     */
    @Override
    public void Save() throws Exception {
    }

    /**
     * Destroys this WrappedPlayer object, saving it and all of its component first,
     * then removes it from the player list.
     */
    @Override
    public void Destroy() {
        scheduler.Free();
        player.Destroy();
        components.values().forEach(c -> {
            try {
                c.Destroy();
            } catch (Exception e) {
                YukiCommonsApi.Get().GetLogger().severe("There was an error destroying %s for player %s.".formatted(c, uuid));
                e.printStackTrace();
            }
        });
        components.clear();
    }

    public final void FinalizeConnection(IAbstractPlayer player) {
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
     * Called when the player has joined the server, but before OnReady is called on player components.
     */
    public void OnReady() {

    }

    /**
     * Called after the player has joined the server, and operations to the player object are safe.
     * On Bukkit, this is called after the <code>PlayerJoinEvent</code> is fired.
     * On BungeeCord, this is called after the <code>PostLoginEvent</code> is fired and <code>FinalizeConnection</code> is called on the wrapped player.
     */
    public void OnJoin() {
        components.values().forEach(PlayerComponent::OnReady);
    }

    /**
     * Called after ALL WrappedPlayer instances registered by every plugin have been initialized.
     */
    public void OnPostInit() {}

    /**
     * Called when the player has logged out and is about to be destroyed, but before data is saved. OnLogout is always called on the main/server thread.
     * <p>
     * OnLogout will be called on each WrapperPlayer instance that logged out before SaveAll is called. Use this to finalize changes that needs to be done on the main thread before the player is destroyed.
     */
    public void OnLogout() {

    }

    public final synchronized void LoadAll() throws Exception {
        dataLoadBegun = true;
        var components = GetDataComponents();
        components.sort(Comparator.comparingDouble(IDataComponent::GetNiceness));

        for (var comp : components) {
            try {
                comp.LoadData();
            } catch (LoginDeniedException e) {
                throw e;

            } catch (Exception e) {
                GetPlugin().GetLogger().severe("There was an error loading %s for player %s.".formatted(comp, uuid));
                throw new RuntimeException(e);
            }
        }

        dataPristine = false;
    }

    public final synchronized void SaveAll() throws Exception {
        var components = GetDataComponents();
        components.sort(Comparator.comparingDouble(c -> -c.GetNiceness()));

        for (var comp : components) {
            try {
                comp.Save();
            } catch (Exception e) {
                GetPlugin().GetLogger().severe("There was an error saving %s for player %s.".formatted(comp, uuid));
                throw e;
            }
        }

        dataPristine = true;
    }

    public IScoreboard GetScoreboard() {
        return player.GetScoreboard();
    }

    public <T extends WrappedPlayer> T GetPlayer(Class<T> clazz) {
        return GetPlayer(uuid, clazz);
    }

    public static <T extends IWrappedPlayer> T GetPlayer(UUID uuid, Class<T> clazz) {
        if (!players.containsKey(clazz)) {
//            System.out.println("players does not contain key!");
            return null;
        }
        return (T) players.get(clazz).get(uuid);
    }

    public static <T extends IWrappedPlayer> T GetPlayer(Object player, Class<T> clazz) {
        if (player == null){
//            System.out.println("player is null!");
            return null;
        }
        IAbstractPlayer abstractPlayer = YukiCommonsApi.Get().GetAdaptor().AdaptToPlayer(player);
        if (abstractPlayer == null){
//            System.out.println("player abs is null!");
            return null;
        }
        return GetPlayer(abstractPlayer.GetUuid(), clazz);
    }

    public static <T extends WrappedPlayer> List<T> GetAllPlayers() {
        return players.values().stream()
                .flatMap(c -> c.values().stream())
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    public static <T extends WrappedPlayer> List<T> GetAllPlayers(Class<T> clazz) {
        if (!players.containsKey(clazz)) {
            return new ArrayList<>();
        }
        return players.get(clazz).values().stream()
                .map(c -> (T) c)
                .collect(Collectors.toList());
    }

    public static void DestroyAll(UUID uuid) {
        for (Map<UUID, WrappedPlayer> map : players.values()) {
            if (map.containsKey(uuid)) {
                try {
                    map.get(uuid).Destroy();
                } catch (Exception e) {
                    YukiCommonsApi.Get().GetLogger().severe("There was an error destroying player %s.".formatted(uuid));
                    e.printStackTrace();
                }
                map.remove(uuid);
            }
        }
    }
}
