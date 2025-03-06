package io.sdkman.broker;

import io.sdkman.broker.download.Platform;

import java.util.Optional;

public enum NativeTarget {
    LINUX_32("i686-unknown-linux-gnu", Platform.LINUX_32),
    LINUX_64("x86_64-unknown-linux-gnu", Platform.LINUX_64),
    LINUX_ARM64("aarch64-unknown-linux-gnu", Platform.LINUX_ARM64),
    MAC_OSX("x86_64-apple-darwin", Platform.MAC_OSX),
    MAC_ARM64("aarch64-apple-darwin", Platform.MAC_ARM64),
    WINDOWS_64("x86_64-pc-windows-msvc", Platform.WINDOWS_64);

    private final String triple;
    private final Platform platform;

    NativeTarget(String triple, Platform platform) {
        this.triple = triple;
        this.platform = platform;
    }

    public static Optional<NativeTarget> of(Platform platform) {
        switch (platform) {
            case LINUX_32:
                return Optional.of(LINUX_32);
            case LINUX_64:
                return Optional.of(LINUX_64);
            case LINUX_ARM64:
                return Optional.of(LINUX_ARM64);
            case MAC_OSX:
                return Optional.of(MAC_OSX);
            case MAC_ARM64:
                return Optional.of(MAC_ARM64);
            case WINDOWS_64:
                return Optional.of(WINDOWS_64);
            default:
                return Optional.empty();
        }
    }

    public String getTriple() {
        return triple;
    }

    public Platform getPlatform() {
        return platform;
    }
}
