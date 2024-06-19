package moe.protasis.yukicommons.api.jooq.impl;

import moe.protasis.yukicommons.api.jooq.IValueSerializer;
import org.apache.commons.lang.NotImplementedException;

public class DefaultValueSerializer implements IValueSerializer<Object, Object> {
    @Override
    public Object SerializeInternal(Object in) {
        throw new NotImplementedException();
    }

    @Override
    public Object DeserializeInternal(Object out) {
        throw new NotImplementedException();
    }

    @Override
    public boolean Passthrough() {
        return true;
    }
}
