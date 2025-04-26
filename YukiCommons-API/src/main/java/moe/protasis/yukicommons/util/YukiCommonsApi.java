package moe.protasis.yukicommons.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import moe.protasis.yukicommons.api.IYukiCommonsApi;

@UtilityClass
public class YukiCommonsApi {
    private static IYukiCommonsApi current;

    public static void SetCurrent(IYukiCommonsApi api) {
        if (current != null) {
            throw new IllegalStateException("YukiCommonsApi is already set to " + current);
        }
        current = api;
    }

    public static IYukiCommonsApi Get() {
        if (current == null) {
            throw new IllegalStateException("YukiCommonsApi is not set");
        }
        return current;
    }
}
