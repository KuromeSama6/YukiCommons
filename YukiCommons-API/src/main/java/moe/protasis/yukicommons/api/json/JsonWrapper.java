package moe.protasis.yukicommons.api.json;

import com.google.gson.*;
import lombok.Getter;
import moe.protasis.yukicommons.util.EnvironmentType;
import moe.protasis.yukicommons.util.Util;
import moe.protasis.yukicommons.util.YukiCommonsApi;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JsonWrapper {
    @Getter
    private final JsonObject json;
    private final Gson gson;

    public JsonWrapper(JsonElement ele) {
        if (!ele.isJsonObject())
            throw new IllegalArgumentException("Argument must be a JsonObject!");

        json = ele.getAsJsonObject();

        gson = GetBuilder().create();
    }

    public JsonWrapper() {
        this(new JsonObject());
    }
    public JsonWrapper(String str) {
        this(GetBuilder().create().fromJson(str, JsonObject.class));
    }

    public String GetString(String path, String def) {
        JsonElement ret = ResolvePath(path);
        return ret != null && ret.isJsonPrimitive() && ret.getAsJsonPrimitive().isString() ? ret.getAsString() : def;
    }
    public String GetString(String path) {
        return GetString(path, null);
    }

    public <T extends Enum<T>> T GetEnum(Class<T> clazz, String path, T def) {
        String str = GetString(path, def.toString());
        if (str == null) return null;

        return T.valueOf(clazz, str);
    }

    public int GetInt(String path, int def) {
        JsonElement ret = ResolvePath(path);
        return ret != null && ret.isJsonPrimitive() && ret.getAsJsonPrimitive().isNumber() ? ret.getAsInt() : def;
    }
    public int GetInt(String path) {
        return GetInt(path, 0);
    }

    public long GetLong(String path, long def) {
        JsonElement ret = ResolvePath(path);
        return ret != null && ret.isJsonPrimitive() && ret.getAsJsonPrimitive().isNumber() ? ret.getAsLong() : def;
    }
    public long GetLong(String path) {
        return GetLong(path, 0);
    }

    public double GetDouble(String path, double def) {
        JsonElement ret = ResolvePath(path);
        return ret != null && ret.isJsonPrimitive() && ret.getAsJsonPrimitive().isNumber() ? ret.getAsDouble() : def;
    }
    public double GetDouble(String path) {
        return GetDouble(path, 0);
    }

    public boolean GetBool(String path, boolean def) {
        JsonElement ret = ResolvePath(path);
        return ret != null && ret.isJsonPrimitive() && ret.getAsJsonPrimitive().isBoolean() ? ret.getAsBoolean() : def;
    }
    public boolean GetBool(String path) {
        return GetBool(path, false);
    }
    public UUID GetUuid(String path) {
        String ret = GetString(path);
        return ret == null ? null : UUID.fromString(ret);
    }

    public <T> List<T> GetList(String path, Class<T> clazz) {
        List<T> ret = new ArrayList<>();
        JsonElement ele = ResolvePath(path);
        if (ele == null || !ele.isJsonArray()) return ret;

        for (JsonElement jsonElement : ele.getAsJsonArray()) {
            try {
                ret.add(gson.fromJson(jsonElement, clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public <T extends Enum<T>> List<T> GetEnumList(String path, Class<T> clazz) {
        List<T> ret = new ArrayList<>();
        JsonElement ele = ResolvePath(path);
        if (ele == null || !ele.isJsonArray()) return ret;

        for (JsonElement jsonElement : ele.getAsJsonArray()) {
            if (!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) continue;

            try {
                ret.add(Enum.valueOf(clazz, jsonElement.getAsString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public JsonArray GetList(String path) {
        JsonElement ele = ResolvePath(path);
        return ele != null && ele.isJsonArray() ? ele.getAsJsonArray() : new JsonArray();
    }

    public <T> T GetObject(String path, Class<T> clazz) {
        JsonElement ret = ResolvePath(path);
        if (ret == null || ret.equals(JsonNull.INSTANCE)) return null;

        return gson.fromJson(ret.toString(), clazz);
    }

    public JsonWrapper GetObject(String path) {
        JsonElement ret = ResolvePath(path);
        if (ret == null || !ret.isJsonObject()) return new JsonWrapper();
        return new JsonWrapper(ret.getAsJsonObject());
    }

    public JsonElement Get(String path) {
        return ResolvePath(path);
    }

    public List<JsonWrapper> GetObjectList(String path) {
        List<JsonWrapper> ret = new ArrayList<>();
        JsonElement node = ResolvePath(path);
        if (node == null || !node.isJsonArray()) return ret;

        for (var jsonElement : node.getAsJsonArray()) {
            ret.add(new JsonWrapper(jsonElement));
        }

        return ret;
    }

    public List<String> GetKeys() {
        return json.entrySet().stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    public boolean Has(String path) {
        return ResolvePath(path) != null;
    }

    public JsonWrapper Merge(String path, JsonWrapper other) {
        JsonElement ret = ResolvePath(path);
        if (ret == null || !ret.isJsonObject())
            throw new IllegalArgumentException("Path must be a JsonObject!");

        var json = ret == this.json ? this : new JsonWrapper(ret);
        for (var key : other.GetKeys()) {
            json.Set(key, other.Get(key));
        }

        return this;
    }

    public JsonWrapper Set(String path, Object obj) {
        JsonObject parent = json;

        String[] args = path.split("\\.");
        if (args.length == 0)
            throw new IllegalArgumentException("Setting parent node not permitted.");
        String property = args[args.length - 1];

        for (int i = 0; i < args.length - 1; i++) {
            String name = args[i];
            if (!parent.has(name) || !parent.get(name).isJsonObject()) parent.add(name, new JsonObject());
            parent = parent.get(name).getAsJsonObject();
        }

        Set(property, parent, obj);
        return this;
    }

    public JsonWrapper SetObject(String path, Object obj) {
        return Set(path, gson.toJsonTree(obj));
    }

    public JsonWrapper Remove(String path) {
        JsonElement parent = ResolvePath(path);
        if (parent == null || !parent.isJsonObject())
            throw new IllegalArgumentException("Path must be a JsonObject!");

        String[] args = path.split("\\.");
        String property = args[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            String name = args[i];
            if (!parent.getAsJsonObject().has(name)) return this;
            parent = parent.getAsJsonObject().get(name);
        }

        parent.getAsJsonObject().remove(property);
        return this;
    }

    public void Save(File file) {
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)){
            GetBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(json, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void Set(String name, JsonObject parent, Object obj) {
        if (obj == null || obj instanceof JsonNull) {
            parent.remove(name);
        }
        else if (obj instanceof Number) parent.addProperty(name, (Number)obj);
        else if (obj instanceof Boolean) parent.addProperty(name, (Boolean)obj);
        else if (obj instanceof Character) parent.addProperty(name, (Character)obj);
        else if (obj instanceof JsonElement) {
            parent.add(name, (JsonElement)obj);
        } else if (obj instanceof Map) {
            JsonObject data = new JsonObject();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>)obj).entrySet()) {
                Set(Objects.toString(entry.getKey()), data, entry.getValue());
            }
            parent.add(name, data);
        } else if (obj instanceof List) {
            JsonArray arr = new JsonArray();
            for (Object o : (List<?>)obj) {
                arr.add(gson.toJsonTree(o));
            }
            parent.add(name, arr);
        } else if (obj instanceof IJsonSerializable) {
            parent.add(name, ((IJsonSerializable)obj).Serialize());
        } else if (Util.GetEnvironment() == EnvironmentType.BUKKIT && obj instanceof ItemStack) {
            parent.add(name, gson.toJsonTree(obj, ItemStack.class));
        } else if (Util.GetEnvironment() == EnvironmentType.BUKKIT && obj instanceof Location) {
            parent.add(name, gson.toJsonTree(obj, Location.class));
        } else if (obj instanceof Enum<?>) {
            parent.addProperty(name, obj.toString());
        }
        else if (obj instanceof JsonWrapper) parent.add(name, ((JsonWrapper)obj).json);
        else parent.addProperty(name, obj.toString());
    }

    private JsonElement ResolvePath(String path) {
        if (path.isEmpty()) return json;
        String[] args = path.split("\\.");
        if (args.length == 0) return json;

        JsonObject ret = json;
        for (String name : args) {
            if (ret.has(name)) {
                JsonElement ele = ret.get(name);
                if (ele.isJsonObject()) ret = ele.getAsJsonObject();
                else return ele;

            } else return null;
        }

        return ret;
    }

    public JsonWrapper Clone() {
        return new JsonWrapper(json.deepCopy());
    }

    @Override
    public String toString() {
        return gson.toJson(json);
    }

    public static GsonBuilder GetBuilder() {
        var builder = new GsonBuilder()
                .disableHtmlEscaping();
        var api = YukiCommonsApi.Get();
        for (var adapter : api.GetJsonTypeAdapters()) {
            builder.registerTypeAdapter(adapter.GetTargetType(), adapter);
        }

        return builder;
    }

    public static JsonWrapper FromYaml(ConfigurationSection sec) {
        if (Util.GetEnvironment() != EnvironmentType.BUKKIT)
            throw new IllegalStateException("This method is only callable on Spigot.");
        JsonWrapper ret = new JsonWrapper();
//        System.out.println("start");

        for (String key : sec.getKeys(false)) {
//            System.out.println(String.format("key: %s", sec.get(key)));

            if (sec.isConfigurationSection(key)) {
//                System.out.println("config section");
                ret.Set(key, FromYaml(sec.getConfigurationSection(key)));
            }
            else if (sec.isList(key)){
//                System.out.println("list");
                ret.Set(key, sec.getList(key));
            }
            else {
//                System.out.println(String.format("primitive %s", sec.get(key).getClass()));
                ret.Set(key, sec.get(key));
            }

//            System.out.println(ret);
        }

//        System.out.println("finished");
        return ret;
    }

}
