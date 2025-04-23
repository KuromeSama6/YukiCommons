package moe.protasis.yukicommons.json.serializer;

import com.google.gson.*;
import moe.protasis.yukicommons.json.JsonWrapper;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Type;

public class PotionEffectSerializer implements JsonSerializer<PotionEffect>, JsonDeserializer<PotionEffect> {
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
}
