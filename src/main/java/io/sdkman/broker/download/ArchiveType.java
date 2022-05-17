package io.sdkman.broker.download;

import java.util.EnumSet;

/**
 * Supported archive types to be returned by the {@link CandidateDownloadHandler} controller.
 */
public enum ArchiveType {
    ZIP("zip", "zip"),
    TAR("tar", "tar.gz"),
    DMG("dmg", "dmg");

    private final String type;
    private final String extension;

    public String type() {
        return type;
    }

    public String extension() {
        return extension;
    }

    ArchiveType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public static ArchiveType fromUrl(String url) {
        return EnumSet.allOf(ArchiveType.class).stream()
                .filter(archiveType -> url.endsWith(archiveType.extension()))
                .findFirst()
                .orElse(ZIP);
    }
}
