package moe.protasis.yukicommons.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import org.joda.time.DateTime;

import java.lang.reflect.Type;

public class DateTimeSerializer implements IJsonTypeAdapter, JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    @Override
    public DateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new DateTime(jsonElement.getAsLong());
    }

    @Override
    public JsonElement serialize(DateTime dateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(dateTime.getMillis());
    }

    @Override
    public Class<?> GetTargetType() {
        return DateTime.class;
    }
}
