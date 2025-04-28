package moe.protasis.yukicommons.bukkit.impl.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.api.json.IJsonTypeAdapter;
import moe.protasis.yukicommons.api.json.JsonWrapper;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class PotionEffectSerializer implements IJsonTypeAdapter, JsonSerializer<PotionEffect>, JsonDeserializer<PotionEffect> {
    @Override
    public PotionEffect deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonWrapper data = new JsonWrapper(jsonElement);
        int duration = data.GetInt("duration");

        return new PotionEffect(
                PotionEffectType.getByName(data.GetString("type")),
                duration >= 0 ? duration : Integer.MAX_VALUE,
                data.GetInt("amp"),
                data.GetBool("ambient"),
                data.GetBool("particles")
        );
    }

    @Override
    public JsonElement serialize(PotionEffect potionEffect, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonWrapper()
                .Set("type", potionEffect.getType().getName())
                .Set("duration", potionEffect.getDuration())
                .Set("amp", potionEffect.getAmplifier())
                .Set("ambient", potionEffect.isAmbient())
                .Set("particles", potionEffect.hasParticles())
                .getJson();
    }

    @Override
    public Class<?> GetTargetType() {
        return PotionEffect.class;
    }
}
