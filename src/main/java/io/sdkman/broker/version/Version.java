package io.sdkman.broker.version;

public class Version {
    private final String candidate;
    private final String version;
    private final String url;
    private final String platform;

    public Version(String candidate, String version, String url, String platform) {
        this.candidate = candidate;
        this.version = version;
        this.url = url;
        this.platform = platform;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getPlatform() {
        return platform;
    }

    @Override
    public String toString() {
        return "Version{" +
                "candidate='" + candidate + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                ", platform='" + platform + '\'' +
                '}';
    }
}
