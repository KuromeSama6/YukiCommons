package moe.protasis.yukicommons.api.entity;

import moe.protasis.yukicommons.api.misc.IDestroyable;
import moe.protasis.yukicommons.api.world.AABB;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Interface that represents an abstract living entity.
 */
public interface IAbstractLivingEntity extends IDestroyable {
    /**
     * @return The entity's name.
     */
    String GetName();
    AABB GetBoundingBox();
    Object GetHandle();

    default Entity GetBukkitEntity() {
        return (Entity)GetHandle();
    }

    default LivingEntity GetBukkitLivingEntity() {
        return (LivingEntity)GetHandle();
    }
}
