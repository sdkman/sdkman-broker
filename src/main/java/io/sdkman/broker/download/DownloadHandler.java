package io.sdkman.broker.download;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.lang.OptionalConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.List;

@Singleton
public class DownloadHandler implements Handler {

    private final static Logger LOG = LoggerFactory.getLogger(DownloadHandler.class);

    private static final String COMMAND = "install";

    private VersionRepo versionRepo;
    private AuditRepo auditRepo;
    private DownloadResolver downloadResolver;

    @Inject
    public DownloadHandler(VersionRepo versionRepo, AuditRepo auditRepo, DownloadResolver downloadResolver) {
        this.versionRepo = versionRepo;
        this.auditRepo = auditRepo;
        this.downloadResolver = downloadResolver;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        OptionalConsumer.of(RequestDetails.of(ctx)).ifPresent(details -> {
                    LOG.info("Received download request for: "
                            + details.getCandidate() + " "
                            + details.getVersion() + " "
                            + details.getPlatform());

                    OptionalConsumer.of(Platform.of(details.getPlatform()))
                            .ifPresent(p -> versionRepo
                                    .fetch(details.getCandidate(), details.getVersion())
                                    .then((List<Version> downloads) -> OptionalConsumer.of(downloadResolver.resolve(downloads, p.name()))
                                            .ifPresent(v -> {
                                                record(details, p.id(), v.getPlatform());
                                                ctx.redirect(302, v.getUrl());
                                            })
                                            .ifNotPresent(() -> ctx.clientError(404))))
                            .ifNotPresent(() -> ctx.clientError(400));
                }
        ).ifNotPresent(() -> ctx.clientError(404));
    }

    private void record(RequestDetails details, String identifier, String platform) {
        try {
            auditRepo.record(
                    AuditEntry.of(COMMAND, details.getCandidate(), details.getVersion(), details.getHost(),
                            details.getAgent(), identifier, platform));
        } catch (Exception e) {
            LOG.error("Unable record audit entry: " + details + " - " + e.getMessage());
        }
    }
}
