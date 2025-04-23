package moe.protasis.yukicommons.api.jooq.impl;

import moe.protasis.yukicommons.api.jooq.IValueSerializer;
import moe.protasis.yukicommons.util.CUID;
import org.joda.time.DateTime;

import java.lang.reflect.Field;

public class CUIDSerializer implements IValueSerializer<CUID, String> {
    @Override
    public Object SerializeInternal(Field field, CUID in) {
        return in.toString();
    }

    @Override
    public CUID DeserializeInternal(Field field, String out) {
        return new CUID(out);
    }
}
