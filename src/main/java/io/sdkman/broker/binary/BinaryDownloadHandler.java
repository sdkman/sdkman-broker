package io.sdkman.broker.binary;

import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.download.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

public class BinaryDownloadHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(BinaryDownloadHandler.class);

    private final BinaryDownloadConfig config;
    private final AuditRepo auditRepo;

    @Inject
    public BinaryDownloadHandler(BinaryDownloadConfig config, AuditRepo auditRepo) {
        this.config = config;
        this.auditRepo = auditRepo;
    }

    @Override
    public void handle(Context ctx) {
        RequestDetails.of(ctx).ifPresentOrElse(details -> {
            logger.info("Received download request for: " + details);
            record(details, inferPlatform(details.getPlatform()));
            if (details.getVersion().startsWith("latest")) {
                ctx.redirect(String.format(prepareRemoteBinaryUrl(), "latest", details.getVersion()));
            } else {
                ctx.redirect(String.format(prepareRemoteBinaryUrl(), details.getVersion(), details.getVersion()));
            }
        }, () -> ctx.clientError(400));
    }

    private String prepareRemoteBinaryUrl() {
        return config.getProtocol() + "://" + config.getHost() + config.getUri() + config.getName();
    }

    private String inferPlatform(String platform) {
        return Platform.of(platform).map(Platform::id).orElse("not defined");
    }

    private void record(RequestDetails details, String platform) {
        auditRepo.record(
                AuditEntry.of(
                        details.getCommand(), "sdkman", details.getVersion(), details.getHost(),
                        details.getAgent(), platform, "UNIVERSAL"));
    }
}
