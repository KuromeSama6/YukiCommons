package moe.protasis.yukicommons.jooq;

import java.lang.reflect.Field;

/**
 * An interface representing a value serializer for a single type in the
 * YukiCommons JOOQ utility.
 * 
 * @param TTarget The original type
 * @param TInput  The type after serialization, of the value that is stored in
 *                the database.
 *                This value must be a value that JOOQ can store and handle, for
 *                instance, a primitive or a string.
 */
public interface IValueSerializer<TTarget, TInput> {
    /**
     * This method is to be implemented by the implmenting class. Serializes the
     * value.
     * 
     * @param field The field on the input object in which the value is stored in.
     * @param in    The input object.
     * @return The serialized value. Ideally, this value should match
     *         <code>TInput</code>.
     */
    Object SerializeInternal(Field field, TTarget in);

    default Object Serialize(Field field, Object in) {
        // null check
        if (in == null)
            return null;
        return SerializeInternal(field, (TTarget) in);
    }

    /**
     * This method is to be implemented by the implementing class. Deserializes the
     * value after fetching it from the database.
     * 
     * @param field The field on the input object in which the value is stored in.
     * @param out   The input object.
     * @return The deserialized value.
     */
    TTarget DeserializeInternal(Field field, TInput out);

    default TTarget Deserialize(Field field, Object in) {
        // null check
        if (in == null)
            return null;
        return DeserializeInternal(field, (TInput) in);
    }

    /**
     * When an IValueSerializer's Passthrough() method returns true, this serializer
     * is simply ignored, and any values originally serialized or deserialized by
     * this serializer simply passes through unchanged.
     * 
     * @return Whether this serialize has passthrough enabled.
     */
    default boolean Passthrough() {
        return false;
    }
}
