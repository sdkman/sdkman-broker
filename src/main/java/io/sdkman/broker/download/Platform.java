package io.sdkman.broker.download;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.stream;

public enum Platform {
    FREE_BSD(newArrayList("FreeBSD")),
    LINUX(newArrayList("Linux")),
    MAC_OSX(newArrayList("Darwin")),
    WINDOWS_32(newArrayList("MINGW32")),
    WINDOWS_64(newArrayList("CYGWIN", "MINGW64", "MSYS")),
    SUN_OS(newArrayList("SunOS"));

    List<String> unames;

    Platform(List<String> uname) {
        this.unames = uname;
    }

    public static Optional<Platform> of(String uname) {
        return stream(Platform.values()).filter(p -> p.unames.stream().anyMatch(uname::startsWith)).findFirst();
    }
}
