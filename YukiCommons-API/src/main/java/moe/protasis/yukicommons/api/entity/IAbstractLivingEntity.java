package moe.protasis.yukicommons.api.entity;

import moe.protasis.yukicommons.api.misc.IDestroyable;
import moe.protasis.yukicommons.api.world.AABB;

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
}
