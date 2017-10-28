package io.sdkman.broker.binary;

import io.sdkman.broker.app.ApplicationRepo;
import io.sdkman.broker.lang.OptionalConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;
import java.util.Optional;

public class BinaryVersionHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(BinaryDownloadHandler.class);

    private final ApplicationRepo appRepo;

    @Inject
    public BinaryVersionHandler(ApplicationRepo appRepo) {
        this.appRepo = appRepo;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        OptionalConsumer.of(Optional.ofNullable(ctx.getPathTokens().get("versionType")))
                .ifPresent(versionType ->
                        appRepo.findVersion(versionType).then(version ->
                                OptionalConsumer.of(Optional.ofNullable(version))
                                        .ifPresent(ctx::render)
                                        .ifNotPresent(() -> ctx.clientError(404))))
                .ifNotPresent(() -> ctx.clientError(400));
    }
}