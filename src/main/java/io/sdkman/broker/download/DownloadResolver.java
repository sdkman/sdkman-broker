package io.sdkman.broker.download;

import io.sdkman.broker.version.Version;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DownloadResolver {

    public static final String UNIVERSAL_PLATFORM = "UNIVERSAL";

    public Optional<Version> resolve(List<Version> versions, String platform) {
        Map<String, Version> versionMap = new HashMap<>() {{
            versions.forEach(v -> put(v.getPlatform(), v));
        }};

        return Optional.ofNullable(
                Optional.ofNullable(versionMap.get(platform))
                        .orElse(versionMap.get(UNIVERSAL_PLATFORM)));
    }
}
