package io.sdkman.broker.download;

import ratpack.handling.Context;
import ratpack.http.Request;
import ratpack.path.PathTokens;

public class RequestDetails {

    public static final String PLATFORM_PARAMETER_NAME = "platform";

    private final String candidate;
    private final String version;
    private final String host;
    private final String agent;
    private final String uname;

    private RequestDetails(String candidate, String version, String host, String agent, String uname) {
        this.candidate = candidate;
        this.version = version;
        this.host = host;
        this.agent = agent;
        this.uname = uname;
    }

    public static RequestDetails of(Context ctx) {
        PathTokens pathTokens = ctx.getAllPathTokens();
        Request request = ctx.getRequest();
        return new RequestDetails(
                pathTokens.get("candidate"),
                pathTokens.get("version"),
                request.getHeaders().get("X-Real-IP"),
                request.getHeaders().get("user-agent"),
                determineNormalisedPlatform(pathTokens, request));
    }

    private static String determineNormalisedPlatform(PathTokens pathTokens, Request request) {
        return pathTokens.get(PLATFORM_PARAMETER_NAME) != null ?
                pathTokens.get(PLATFORM_PARAMETER_NAME) :
                request.getQueryParams().get(PLATFORM_PARAMETER_NAME).toLowerCase();
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

    public String getUname() {
        return uname;
    }

    @Override
    public String toString() {
        return "RequestDetails{" +
                "candidate='" + candidate + '\'' +
                ", version='" + version + '\'' +
                ", host='" + host + '\'' +
                ", agent='" + agent + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }
}
