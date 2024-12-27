package moe.protasis.yukicommons.api.player;

/**
 * The method with which a data for an entity (i.e. a WrappedPlayer or a Component) shall be loaded.
 */
public enum EntityLoadType {
    /**
     * The data will be loaded synchronously, blocking the current thread.
     */
    BLOCKING,
    /**
     * The data will be loaded asynchronously, using a separate thread. A player will not be able to interact
     * with the game before all WrappedPlayer instances and PlayerComponent instances with this load type have
     * been loaded. It is guaranteed that the data will be loaded before the player can interact with the game.
     */
    PRIORITY,
    /**
     * The data will be loaded asynchronously, using a separate thread. No guarantees are made about loaded data.
     */
    REGULAR
}
