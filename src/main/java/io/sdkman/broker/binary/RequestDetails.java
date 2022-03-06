package io.sdkman.broker.binary;

import ratpack.handling.Context;
import ratpack.http.Headers;
import ratpack.http.Request;
import ratpack.path.PathTokens;

import java.util.Optional;

public class RequestDetails {

    private static final String COMMAND_PATH_TOKEN_NAME = "command";
    private static final String VERSION_PATH_TOKEN_NAME = "version";
    private static final String PLATFORM_PATH_TOKEN_NAME = "platform";
    private static final String HOST_HEADER_NAME = "X-Real-IP";
    private static final String AGENT_HEADER_NAME = "user-agent";

    private final String command;
    private final String version;
    private final String platform;
    private final String host;
    private final String agent;

    private RequestDetails(String command, String version, String platform, String host, String agent) {
        this.command = command;
        this.version = version;
        this.platform = platform;
        this.host = host;
        this.agent = agent;
    }

    public static Optional<RequestDetails> of(Context ctx) {
        Request request = ctx.getRequest();
        Headers headers = request.getHeaders();

        if (isValidRequest(ctx)) {
            PathTokens tokens = ctx.getAllPathTokens();
            return Optional.of(
                    new RequestDetails(
                            tokens.get(COMMAND_PATH_TOKEN_NAME),
                            tokens.get(VERSION_PATH_TOKEN_NAME),
                            tokens.get(PLATFORM_PATH_TOKEN_NAME),
                            headers.get(HOST_HEADER_NAME),
                            headers.get(AGENT_HEADER_NAME)));
        } else {
            return Optional.empty();
        }
    }

    private static boolean isValidRequest(Context ctx) {
        PathTokens tokens = ctx.getAllPathTokens();
        return tokens.get(COMMAND_PATH_TOKEN_NAME) != null
                && tokens.get(VERSION_PATH_TOKEN_NAME) != null
                && tokens.get(PLATFORM_PATH_TOKEN_NAME) != null;
    }

    public String getCommand() {
        return command;
    }

    public String getVersion() {
        return version;
    }

    public String getPlatform() {
        return platform;
    }

    public String getHost() {
        return host;
    }

    public String getAgent() {
        return agent;
    }

    @Override
    public String toString() {
        return "RequestDetails{" +
                "command='" + command + '\'' +
                ", version='" + version + '\'' +
                ", platform='" + platform + '\'' +
                ", host='" + host + '\'' +
                ", agent='" + agent + '\'' +
                '}';
    }
}
