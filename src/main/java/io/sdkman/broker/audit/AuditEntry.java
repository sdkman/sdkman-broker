package io.sdkman.broker.audit;

import io.sdkman.broker.download.RequestDetails;

public class AuditEntry {
    private final String command;
    private final String candidate;
    private final String version;
    private final String host;
    private final String agent;
    private final String platform;
    private final String uname;
    private final Long timestamp = System.currentTimeMillis();

    public AuditEntry(String command, String candidate, String version, String host, String agent, String platform, String uname) {
        this.command = command;
        this.candidate = candidate;
        this.version = version;
        this.host = host;
        this.agent = agent;
        this.platform = platform;
        this.uname = uname;
    }

    public String getCommand() {
        return command;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public String getAgent() {
        return agent;
    }

    public String getPlatform() {
        return platform;
    }

    public String getUname() {
        return uname;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "AuditEntry{" +
                "command='" + command + '\'' +
                ", candidate='" + candidate + '\'' +
                ", version='" + version + '\'' +
                ", host='" + host + '\'' +
                ", agent='" + agent + '\'' +
                ", platform='" + platform + '\'' +
                ", uname='" + uname + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public static AuditEntry of(String command, RequestDetails d, String platform) {
        return new AuditEntry(command, d.getCandidate(), d.getVersion(), d.getHost(), d.getAgent(), platform, d.getUname());
    }
}
