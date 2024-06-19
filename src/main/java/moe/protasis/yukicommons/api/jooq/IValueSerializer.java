package moe.protasis.yukicommons.api.jooq;

public interface IValueSerializer<TTarget, TInput> {
    Object SerializeInternal(TTarget in);
    default Object Serialize(Object in) {
        return SerializeInternal((TTarget)in);
    }
    TTarget DeserializeInternal(TInput out);
    default TTarget Deserialize(Object in) {
        return DeserializeInternal((TInput)in);
    }

    default boolean Passthrough() {
        return false;
    }
}
