package moe.protasis.yukicommons.api.player;

/**
 * Represents something that is part of player data and can be loaded and saved
 */
public interface IDataComponent {
    void LoadData() throws Exception;
    void Save() throws Exception;

    /**
     * Returns the niceness of this component. Components with lower niceness are loaded first and saved last.
     * @return The niceness of this component.
     */
    default double GetNiceness() {
        return 0;
    }
}
