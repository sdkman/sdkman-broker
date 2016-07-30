package io.sdkman.broker;

import ratpack.server.RatpackServer;

import static ratpack.jackson.Jackson.json;

public class Main {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(spec ->
                spec.handlers(chain ->
                        chain.get("alive", ctx ->
                                ctx.render(json(new HealthResponse("OK"))))));
    }
}
