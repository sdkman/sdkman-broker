package io.sdkman.broker.binary;

import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.download.Platform;
import io.sdkman.broker.lang.OptionalConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

public class BinaryDownloadHandler implements Handler {

    private final static Logger LOG = LoggerFactory.getLogger(BinaryDownloadHandler.class);

    private final BinaryDownloadConfig config;
    private final AuditRepo auditRepo;

    @Inject
    public BinaryDownloadHandler(BinaryDownloadConfig config, AuditRepo auditRepo) {
        this.config = config;
        this.auditRepo = auditRepo;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        OptionalConsumer.of(RequestDetails.of(ctx)).ifPresent(d -> {
                    LOG.info("Received " + d.getCommand() + " download request for: sdkman " +
                            " " + d.getVersion() + " " + d.getPlatform());

                    record(d, inferPlatform(d.getPlatform()));
                    ctx.redirect(String.format(prepareRemoteBinaryUrl(), d.getVersion()));
                }
        ).ifNotPresent(() -> ctx.clientError(400));
    }

    private String prepareRemoteBinaryUrl() {
        return config.getProtocol() + "://" + config.getHost() + config.getUri() + config.getName();
    }

    private String inferPlatform(String platform) {
        return Platform.of(platform).map(Platform::id).orElse("n/a");
    }

    private void record(RequestDetails details, String platform) {
        try {
            auditRepo.record(
                    AuditEntry.of(
                            details.getCommand(), "sdkman", details.getVersion(), details.getHost(),
                            details.getAgent(), platform, "UNIVERSAL"));
        } catch (Exception e) {
            LOG.error("Unable record audit entry: " + details + " - " + e.getMessage());
        }
    }
}
