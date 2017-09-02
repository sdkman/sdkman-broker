package io.sdkman.broker.binary;

import ratpack.handling.Context;
import ratpack.http.Headers;
import ratpack.http.Request;
import ratpack.path.PathTokens;

import java.util.Optional;

public class RequestDetails {
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
                            tokens.get("command"),
                            tokens.get("version"),
                            tokens.get("platform"),
                            headers.get("X-Real-IP"),
                            headers.get("user-agent")));
        } else {
            return Optional.empty();
        }
    }

    private static boolean isValidRequest(Context ctx) {
        PathTokens tokens = ctx.getAllPathTokens();
        return tokens.get("command") != null
                && tokens.get("version") != null
                && tokens.get("platform") != null;
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
}
