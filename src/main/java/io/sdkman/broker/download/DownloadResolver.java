package io.sdkman.broker.download;

import java.util.List;
import java.util.Optional;

public class DownloadResolver {

    public static final String UNIVERSAL_PLATFORM = "UNIVERSAL";

    public Optional<Version> resolve(List<Version> versions, String platform) {
        Optional<Version> universalVersion = platformVersion(versions, UNIVERSAL_PLATFORM);
        if (universalVersion.isPresent()) {
            return universalVersion;
        } else {
            return platformVersion(versions, platform);
        }
    }

    private Optional<Version> platformVersion(List<Version> versions, String platform) {
        return versions.stream()
                .filter(v -> platform.equals(v.getPlatform()))
                .findFirst();
    }
}
