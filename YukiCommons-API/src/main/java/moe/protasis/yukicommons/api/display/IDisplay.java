package moe.protasis.yukicommons.api.display;

public interface IDisplay<T extends IDisplayProvider> {
    void Update();

    /**
     * Gets the active provider. A display may have multiple providers.
     *
     * @return The active provider, or null if none is active.
     */
    T GetActiveProvider();

    /**
     * Pushes a provider onto the top of the provider stack. The topmost provider will be used to display the scoreboard.
     * If the provider is already on the stack, it will be moved to the top.
     *
     * @param provider The provider to add.
     */
    void AddProvider(T provider);

    /**
     * Removes the given provider from the display. If the provider is not on the stack, it will not be removed.
     *
     * @param provider The provider to remove.
     */
    void RemoveProvider(T provider);

    void RemoveAllProviders();
}
