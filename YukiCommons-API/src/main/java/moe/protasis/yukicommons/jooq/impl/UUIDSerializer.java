package moe.protasis.yukicommons.jooq.impl;

import moe.protasis.yukicommons.jooq.IValueSerializer;

import java.lang.reflect.Field;
import java.util.UUID;

public class UUIDSerializer implements IValueSerializer<UUID, String> {
    @Override
    public Object SerializeInternal(Field field, UUID in) {
        return in.toString();
    }

    @Override
    public UUID DeserializeInternal(Field field, String out) {
        return UUID.fromString(out);
    }
}
