package io.sdkman.broker.download;

import ratpack.handling.Context;
import ratpack.http.Request;
import ratpack.path.PathTokens;
import ratpack.util.MultiValueMap;

import java.util.Map;
import java.util.Optional;

public class RequestDetails {

    public static final String PLATFORM_PARAMETER_NAME = "platform";
    public static final String CANDIDATE_PARAMETER_NAME = "candidate";
    public static final String VERSION_PARAMETER_NAME = "version";

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

    public static Optional<RequestDetails> of(Context ctx) {
        PathTokens pathTokens = ctx.getAllPathTokens();
        Request request = ctx.getRequest();
        if (isValidRequest(ctx)) {
            return Optional.of(
                    new RequestDetails(
                            pathTokens.get("candidate"),
                            pathTokens.get("version"),
                            request.getHeaders().get("X-Real-IP"),
                            request.getHeaders().get("user-agent"),
                            determineNormalisedPlatform(pathTokens, request)));
        } else {
            return Optional.empty();
        }
    }

    private static boolean isValidRequest(Context ctx) {
        PathTokens pathTokens = ctx.getAllPathTokens();
        MultiValueMap<String, String> queryParams = ctx.getRequest().getQueryParams();
        return isParameterPresentIn(pathTokens, CANDIDATE_PARAMETER_NAME) &&
                isParameterPresentIn(pathTokens, VERSION_PARAMETER_NAME) &&
                isParameterPresentIn(pathTokens, PLATFORM_PARAMETER_NAME) ||
                isParameterPresentIn(queryParams, PLATFORM_PARAMETER_NAME);
    }

    private static String determineNormalisedPlatform(PathTokens pathTokens, Request request) {
        return isParameterPresentIn(pathTokens, PLATFORM_PARAMETER_NAME) ?
                pathTokens.get(PLATFORM_PARAMETER_NAME) :
                request.getQueryParams().get(PLATFORM_PARAMETER_NAME).toLowerCase();
    }

    private static boolean isParameterPresentIn(Map map, String key) {
        return map.get(key) != null;
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
