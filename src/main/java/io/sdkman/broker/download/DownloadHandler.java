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
        PathTokens pathTokens = ctx.getAllPathTokens();
        String candidate = pathTokens.get("candidate");
        String version = pathTokens.get("version");
        String host = ctx.getRequest().getHeaders().get("X-Real-IP");
        String agent = ctx.getRequest().getHeaders().get("user-agent");
        String uname = ctx.getRequest().getQueryParams().get("platform");
        LOG.info("Received download request for: " + candidate + " " + version);

        if (!Platform.of(uname).isPresent()) ctx.clientError(400);
        else Platform.of(uname).ifPresent(platform -> versionRepo
                .fetch(candidate, version)
                .then((List<Version> downloads) -> {
                    Optional<Version> resolved = downloadResolver.resolve(downloads, platform.name());

                    if (!resolved.isPresent()) ctx.clientError(404);

                    resolved.ifPresent(v -> {
                        recordAudit(candidate, version, host, agent, v.getPlatform());
                        ctx.redirect(302, v.getUrl());
                    });
                }));
    }

    private void recordAudit(String candidate, String version, String host, String agent, String platform) {
        AuditEntry auditEntry = new AuditEntry(COMMAND, candidate, version, host, agent, platform);
        try {
            auditRepo.record(auditEntry);
        } catch (Exception e) {
            LOG.error("Unable record audit entry: " + auditEntry + " - " + e.getMessage());
        }
    }
}
