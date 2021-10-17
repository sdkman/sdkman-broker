package io.sdkman.broker.download;

import io.sdkman.model.Version;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DownloadResolver {

    public static final String UNIVERSAL_PLATFORM = "UNIVERSAL";

    public Optional<Version> resolve(List<Version> versions, String platform) {
        Map<String, Version> versionMap = versions.stream()
            .collect(Collectors.toMap(
                Version::platform,
                v -> v
            ));

        return Optional.ofNullable(
                Optional.ofNullable(versionMap.get(platform))
                        .orElse(versionMap.get(UNIVERSAL_PLATFORM)));
    }
}
