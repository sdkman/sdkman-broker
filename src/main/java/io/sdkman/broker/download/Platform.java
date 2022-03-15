package io.sdkman.broker.download;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.stream;

public enum Platform {
    FREE_BSD(newArrayList("FreeBSD")),
    LINUX_ARM32SF(newArrayList("LinuxARM32SF")),
    LINUX_ARM32HF(newArrayList("LinuxARM32HF")),
    LINUX_ARM64(newArrayList("LinuxARM64")),
    LINUX_32(newArrayList("LinuxX32", "Linux32")),
    LINUX_64(newArrayList("LinuxX64", "Linux64", "Linux")),
    MAC_ARM64(newArrayList("DarwinARM64")),
    MAC_OSX(newArrayList("DarwinX64", "Darwin")),
    WINDOWS_32(newArrayList("WindowsX32", "MINGW32")),
    WINDOWS_64(newArrayList("WindowsX64", "CYGWIN", "MINGW64", "MSYS")),
    SUN_OS(newArrayList("SunOS")),
    EXOTIC(newArrayList("Exotic"));

    List<String> ids;

    Platform(List<String> ids) {
        this.ids = ids;
    }

    public static Optional<Platform> of(String id) {
        return Optional.ofNullable(id)
                .map(String::toLowerCase)
                .flatMap(pid -> stream(Platform.values())
                        .filter(platform -> platform.ids.stream().map(String::toLowerCase).anyMatch(pid::startsWith))
                        .findFirst());
    }

    public String id() {
        return ids.get(0);
    }
}
