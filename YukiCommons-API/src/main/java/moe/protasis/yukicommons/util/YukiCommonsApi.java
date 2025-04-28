package moe.protasis.yukicommons.util;

import lombok.experimental.UtilityClass;
import moe.protasis.yukicommons.api.IYukiCommonsAPI;

@UtilityClass
public class YukiCommonsApi {
    private static IYukiCommonsAPI current;

    public static void SetCurrent(IYukiCommonsAPI api) {
        if (current != null) {
            throw new IllegalStateException("YukiCommonsApi is already set to " + current);
        }
        current = api;
    }

    public static IYukiCommonsAPI Get() {
        if (current == null) {
            throw new IllegalStateException("YukiCommonsApi is not set");
        }
        return current;
    }
}
