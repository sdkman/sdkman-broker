package io.sdkman.broker;

import io.sdkman.broker.db.MongoConfig;
import io.sdkman.broker.db.MongoProvider;
import io.sdkman.broker.download.DownloadHandler;
import io.sdkman.broker.download.DownloadResolver;
import io.sdkman.broker.health.MongoHealthCheck;
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
                        .bind(HealthCheckHandler.class)
                        .bind(DownloadResolver.class)
                        .bind(DownloadHandler.class)))
                .handlers(chain -> chain
                        .get("health/:name?", HealthCheckHandler.class)
                        .get("download/:candidate/:version", DownloadHandler.class)));
    }
}
