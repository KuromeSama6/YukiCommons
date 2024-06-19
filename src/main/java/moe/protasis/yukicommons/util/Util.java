package moe.protasis.yukicommons.util;

import lombok.experimental.UtilityClass;
import moe.protasis.yukicommons.YukiCommons;
import moe.protasis.yukicommons.YukiCommonsBungee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@UtilityClass
public class Util {
    public static String ReadToStringAutoClose(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String ret = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            is.close();
            return ret;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<Void> RunAsync(Runnable func) {
        return CompletableFuture.runAsync(() -> {
            try {
                func.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public static void EnsureEnv(EnvironmentType env) {
        if (env != GetEnvironment()) throw new IllegalStateException(String.format("Invalid environment, current environment is %s", GetEnvironment()));
    }

    public static EnvironmentType GetEnvironment() {
        try {
            if (YukiCommonsBungee.getInstance() != null) return EnvironmentType.PROXY;
            else return EnvironmentType.SPIGOT;
        } catch (NoClassDefFoundError e) {
            return EnvironmentType.SPIGOT;
        }
    }
}
