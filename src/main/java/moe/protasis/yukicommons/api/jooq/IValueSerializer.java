package moe.protasis.yukicommons.api.jooq;

import java.lang.reflect.Field;

public interface IValueSerializer<TTarget, TInput> {
    Object SerializeInternal(Field field, TTarget in);
    default Object Serialize(Field field, Object in) {
        // null check
        if (in == null) return null;
        return SerializeInternal(field, (TTarget)in);
    }
    TTarget DeserializeInternal(Field field, TInput out);
    default TTarget Deserialize(Field field, Object in) {
        // null check
        if (in == null) return null;
        return DeserializeInternal(field, (TInput)in);
    }

    default boolean Passthrough() {
        return false;
    }
}
