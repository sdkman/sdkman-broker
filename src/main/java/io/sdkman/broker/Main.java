package io.sdkman.broker;

import io.sdkman.broker.db.MongoConfig;
import io.sdkman.broker.db.MongoProvider;
import io.sdkman.broker.download.DownloadHandler;
import io.sdkman.broker.download.DownloadResolver;
import io.sdkman.broker.health.MongoHealthCheck;
import io.sdkman.broker.version.VersionConfig;
import io.sdkman.broker.version.VersionHandler;
import ratpack.guice.Guice;
import ratpack.health.HealthCheckHandler;
import ratpack.server.RatpackServer;

import static com.google.common.io.Resources.asByteSource;
import static com.google.common.io.Resources.getResource;

public class Main {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(spec -> spec
                .serverConfig(c -> c
                        .props(asByteSource(getResource("version.properties")))
                        .env()
                        .require("/broker", VersionConfig.class)
                        .require("/mongo", MongoConfig.class))
                .registry(Guice.registry(g -> g
                        .bind(MongoProvider.class)
                        .bind(MongoHealthCheck.class)
                        .bind(HealthCheckHandler.class)
                        .bind(VersionHandler.class)
                        .bind(DownloadResolver.class)
                        .bind(DownloadHandler.class)))
                .handlers(chain -> chain
                        .get("health/:name?", HealthCheckHandler.class)
                        .get("version", VersionHandler.class)
                        .get("download/:candidate/:version", DownloadHandler.class)));
    }
}
