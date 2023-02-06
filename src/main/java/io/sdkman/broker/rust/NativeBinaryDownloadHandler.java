package io.sdkman.broker.rust;

import io.sdkman.broker.NativeTarget;
import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.binary.RequestDetails;
import io.sdkman.broker.download.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

import static java.lang.String.format;

public class NativeBinaryDownloadHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(NativeBinaryDownloadHandler.class);

    private static final String CANDIDATE_TYPE = "sdkman_native";

    private final NativeBinaryDownloadConfig config;
    private final AuditRepo auditRepo;

    @Inject
    public NativeBinaryDownloadHandler(NativeBinaryDownloadConfig config, AuditRepo auditRepo) {
        this.config = config;
        this.auditRepo = auditRepo;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        RequestDetails.of(ctx).ifPresentOrElse(details -> {
            logger.info("Received native download request for: {}", details);
            String version = details.getVersion();
            Platform.of(details.getPlatform()).ifPresentOrElse(platform ->
                            NativeTarget.of(platform).ifPresentOrElse((target -> {
                                        auditRepo.insertAudit(AuditEntry.of(details, CANDIDATE_TYPE, platform.id(), platform.name()));
                                        ctx.redirect(format(prepareRemoteBinaryUrl(), version, version, target.getTriple()));
                                    }),
                                    () -> ctx.clientError(404)),
                    () -> ctx.clientError(404));
        }, () -> ctx.clientError(400));
    }

    private String prepareRemoteBinaryUrl() {
        return config.getProtocol() + "://" + config.getHost() + config.getUri() + config.getName();
    }
}
