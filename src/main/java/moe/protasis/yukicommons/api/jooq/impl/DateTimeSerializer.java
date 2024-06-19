package moe.protasis.yukicommons.api.jooq.impl;

import moe.protasis.yukicommons.api.jooq.IValueSerializer;
import org.joda.time.DateTime;

public class DateTimeSerializer implements IValueSerializer<DateTime, Long> {
    @Override
    public Object SerializeInternal(DateTime in) {
        return in.getMillis();
    }

    @Override
    public DateTime DeserializeInternal(Long out) {
        return new DateTime(out);
    }
}
