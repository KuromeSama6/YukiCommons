package moe.protasis.yukicommons.jooq.impl;

import moe.protasis.yukicommons.jooq.IValueSerializer;
import org.joda.time.DateTime;

import java.lang.reflect.Field;

public class DateTimeSerializer implements IValueSerializer<DateTime, Long> {
    @Override
    public Object SerializeInternal(Field field, DateTime in) {
        return in.getMillis();
    }

    @Override
    public DateTime DeserializeInternal(Field field, Long out) {
        return new DateTime(out);
    }
}
