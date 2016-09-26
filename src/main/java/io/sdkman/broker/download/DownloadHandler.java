package io.sdkman.broker.download;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.path.PathTokens;

import java.util.List;
import java.util.Optional;

@Singleton
public class DownloadHandler implements Handler {

    private final static Logger LOG = LoggerFactory.getLogger(DownloadHandler.class);

    private final static String COMMAND = "install";

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
        RequestDetails details = RequestDetails.of(ctx);
        LOG.info("Received download request for: " + details.getCandidate() + " " + details.getVersion());

        if (!Platform.of(details.getUname()).isPresent()) ctx.clientError(400);
        else Platform.of(details.getUname()).ifPresent(platform -> versionRepo
                .fetch(details.getCandidate(), details.getVersion())
                .then((List<Version> downloads) -> {
                    Optional<Version> resolved = downloadResolver.resolve(downloads, platform.name());

                    if (!resolved.isPresent()) ctx.clientError(404);

                    resolved.ifPresent(v -> {
                        record(AuditEntry.of(COMMAND, details));
                        ctx.redirect(302, v.getUrl());
                    });
                }));
    }

    private void record(AuditEntry auditEntry) {
        try {
            auditRepo.record(auditEntry);
        } catch (Exception e) {
            LOG.error("Unable record audit entry: " + auditEntry + " - " + e.getMessage());
        }
    }
}
