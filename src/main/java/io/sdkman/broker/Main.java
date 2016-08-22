package io.sdkman.broker;

import ratpack.guice.Guice;
import ratpack.health.HealthCheckHandler;
import ratpack.server.RatpackServer;

public class Main {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(spec -> spec
                .serverConfig(c -> c
                        .env()
                        .require("/mongo", MongoConfig.class))
                .registry(Guice.registry(g -> g
                        .bind(MongoProvider.class)
                        .bind(MongoHealthCheck.class)
                        .bind(HealthCheckHandler.class)))
                .handlers(chain -> chain
                        .get("health/:name?", HealthCheckHandler.class)));
    }
}
