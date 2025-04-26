package moe.protasis.yukicommons.api.display;

public interface IScoreboard extends IDisplay{
    /**
     * Gets the active scoreboard provider. A scoreboard may have multiple providers.
     * @return The active scoreboard provider, or null if none is active.
     */
    IScoreboardProvider GetActiveProvider();

    /**
     * Pushes a provider onto the top of the provider stack. The topmost provider will be used to display the scoreboard.
     * If the provider is already on the stack, it will be moved to the top.
     * @param provider The provider to add.
     */
    void AddProvider(IScoreboardProvider provider);

    /**
     * Removes the given provider from the scoreboard. If the provider is not on the stack, it will not be removed.
     * @param provider The provider to remove.
     */
    void RemoveProvider(IScoreboardProvider provider);
}
