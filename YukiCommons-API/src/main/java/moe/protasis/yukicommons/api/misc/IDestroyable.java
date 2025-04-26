package moe.protasis.yukicommons.api.misc;

/**
 * Interface for objects that can be destroyed.
 * <p>
 * This interface is used to mark objects that can be destroyed, such as players or entities.
 * Implementing classes should provide their own implementation of the destroy method.
 */
public interface IDestroyable {
    void Destroy();
}
