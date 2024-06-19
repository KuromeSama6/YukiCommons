package moe.protasis.yukicommons.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface IJsonSerializable {
    default JsonElement Serialize() {
        return new Gson().toJsonTree(this);
    }
}
