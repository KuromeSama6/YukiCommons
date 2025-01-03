package moe.protasis.yukicommons.api.player;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a component of a player. A single player may only have one
 * component of a type.
 * For instance, in a roleplaying plugin, a developer may decide that each
 * player has his experience points, and a wallet. In this scenario, the
 * developer may choose to make these two items player components, by extending
 * the <code>PlayerComponent</code> class.
 * 
 * @param <T> The type of this component's owner.
 */
public abstract class PlayerComponent<T extends WrappedPlayer> {
    /**
     * The owner of this component.
     */
    @Getter
    protected final T owner;
    @Getter @Setter
    protected boolean dataReady;

    public PlayerComponent(T owner) {
        this.owner = owner;
    }

    /**
     * ASYNC. Called when this component is created and is to have its data loaded,
     * for instance, from a database. It is advised to perform database query logic
     * in a blocking fashion in this method.
     */
    protected void LoadData() {}

    /**
     * ASYNC. Called when this component is to have its data saved, for instance, to
     * a database. It is advised to perform database query logic in a blocking
     * fashion in this method.
     */
    public void Save() {}

    public void Destroy() {}
    public void Update() {}
    public EntityLoadType GetLoadType() {
        return EntityLoadType.REGULAR;
    }
}
