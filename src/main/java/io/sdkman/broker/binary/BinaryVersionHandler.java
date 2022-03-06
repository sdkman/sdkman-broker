package io.sdkman.broker.binary;

import io.sdkman.broker.app.AppRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;
import java.util.Optional;

public class BinaryVersionHandler implements Handler {

    private static final String VERSION_TYPE_PATH_TOKEN = "versionType";

    private static final Logger logger = LoggerFactory.getLogger(BinaryDownloadHandler.class);

    private final AppRepo appRepo;

    @Inject
    public BinaryVersionHandler(AppRepo appRepo) {
        this.appRepo = appRepo;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        logger.info("Received request for SDKMAN binary: {}", ctx.getPathTokens().get(VERSION_TYPE_PATH_TOKEN));
        Optional.ofNullable(ctx.getPathTokens().get(VERSION_TYPE_PATH_TOKEN))
                .ifPresentOrElse(versionType ->
                                appRepo.findVersion(versionType).then(version ->
                                        Optional.ofNullable(version)
                                                .ifPresentOrElse(ctx::render, clientError(ctx, 404))),
                        clientError(ctx, 400));
    }

    private Runnable clientError(Context ctx, int code) {
        return () -> ctx.clientError(code);
    }

}