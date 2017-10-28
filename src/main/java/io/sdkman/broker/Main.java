package io.sdkman.broker;

import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.binary.BinaryDownloadConfig;
import io.sdkman.broker.binary.BinaryDownloadHandler;
import io.sdkman.broker.binary.BinaryVersionHandler;
import io.sdkman.broker.db.MongoConfig;
import io.sdkman.broker.db.MongoProvider;
import io.sdkman.broker.download.CandidateDownloadHandler;
import io.sdkman.broker.download.VersionRepo;
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
                        .props(asByteSource(getResource("binary.properties")))
                        .env()
                        .sysProps()
                        .require("/broker", VersionConfig.class)
                        .require("/mongo", MongoConfig.class)
                        .require("/binary", BinaryDownloadConfig.class))
                .registry(Guice.registry(g -> g
                        .bind(MongoProvider.class)
                        .bind(MongoHealthCheck.class)
                        .bind(HealthCheckHandler.class)
                        .bind(VersionHandler.class)
                        .bind(AuditRepo.class)
                        .bind(VersionRepo.class)
                        .bind(CandidateDownloadHandler.class)
                        .bind(BinaryVersionHandler.class)
                        .bind(BinaryDownloadHandler.class)))
                .handlers(chain -> chain
                        .get("health/:name?", HealthCheckHandler.class)
                        .get("version", VersionHandler.class)
                        .get("download/sdkman/version/:versionType", BinaryVersionHandler.class)
                        .get("download/sdkman/:command/:version/:platform", BinaryDownloadHandler.class)
                        .get("download/:candidate/:version/:platform", CandidateDownloadHandler.class)
                        .get("download/:candidate/:version", CandidateDownloadHandler.class)));
    }
}
