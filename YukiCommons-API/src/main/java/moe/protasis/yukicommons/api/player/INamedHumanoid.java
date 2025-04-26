package moe.protasis.yukicommons.api.player;

import moe.protasis.yukicommons.api.entity.IAbstractLivingEntity;
import moe.protasis.yukicommons.api.misc.IDestroyable;
import org.bukkit.entity.Player;

/**
 * Represents an entity that has a name. This entity may not nessecarily be a player.
 */
public interface INamedHumanoid extends IDestroyable {
    /**
     * @return Whether this entity is an actual entity in the world.
     */
    boolean IsOnline();
    /**
     * @return The entity.
     */
    IAbstractLivingEntity GetEntity();
    /**
     * @return The name of this entity.
     */
    String GetName();

    /**
     * @return Whether this entity is a player.
     */
    default boolean IsPlayer() {
        return GetPlayer() != null;
    }

    /**
     * @return This entity as a <code>Player</code>. If this entity is not an instance of <code>Player</code>, return null.
     * @see Player
     */
    default IAbstractPlayer GetPlayer() {
        if (GetEntity() instanceof IAbstractPlayer) return (IAbstractPlayer) GetEntity();
        else return null;
    }

}
