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

        versionRepo.resolveDownloadUrl(candidate, version)
                .then(results -> {
                    if (!results.isEmpty()) {
                        Optional<Platform> maybePlatform = Platform.of(uname);
                        if (maybePlatform.isPresent()) {
                            Optional<Version> resolved = downloadResolver.resolve(results, maybePlatform.get().toString());
                            if (resolved.isPresent()) {
                                recordAudit(candidate, version, host, agent, resolved);
                                ctx.redirect(302, resolved.map(Version::getUrl).get());
                            } else ctx.clientError(404);
                        } else ctx.clientError(400);
                    } else ctx.clientError(404);
                });
    }

    private void recordAudit(String candidate, String version, String host, String agent, Optional<Version> maybeResolved) {
        AuditEntry auditEntry = new AuditEntry(
                COMMAND, candidate, version, host, agent,
                maybeResolved.map(Version::getPlatform).orElse("UNKNOWN"));

        try {
            auditRepo.record(auditEntry);
        } catch (Exception e) {
            LOG.error("Unable record audit entry: " + auditEntry + " - " + e.getMessage());
        }
    }
}
