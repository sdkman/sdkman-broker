package io.sdkman.broker.download;

import ratpack.handling.Context;
import ratpack.http.Request;
import ratpack.path.PathTokens;

public class RequestDetails {

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
                request.getQueryParams().get("platform"));
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
}
