package moe.protasis.yukicommons.api.jooq.impl;

import moe.protasis.yukicommons.api.jooq.IValueSerializer;

import java.util.UUID;

public class UUIDSerializer implements IValueSerializer<UUID, String> {
    @Override
    public Object SerializeInternal(UUID in) {
        return in.toString();
    }

    @Override
    public UUID DeserializeInternal(String out) {
        return UUID.fromString(out);
    }
}
