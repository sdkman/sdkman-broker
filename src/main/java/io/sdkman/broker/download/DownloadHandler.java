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

@Singleton
public class DownloadHandler implements Handler {

    private final static Logger LOG = LoggerFactory.getLogger(DownloadHandler.class);

    private final static String COMMAND = "install";

    private VersionRepo versionRepo;
    private AuditRepo auditRepo;

    @Inject
    public DownloadHandler(VersionRepo versionRepo, AuditRepo auditRepo) {
        this.versionRepo = versionRepo;
        this.auditRepo = auditRepo;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        PathTokens pathTokens = ctx.getAllPathTokens();
        String candidate = pathTokens.get("candidate");
        String version = pathTokens.get("version");
        String host = ctx.getRequest().getHeaders().get("X-Real-IP");
        String agent = ctx.getRequest().getHeaders().get("user-agent");
        String platform = ctx.getRequest().getQueryParams().get("platform");
        LOG.info("Received download request for: " + candidate + " " + version);
        versionRepo.resolveDownloadUrl(candidate, version)
                .then(result -> {
                    if (result.isPresent()) {
                        auditRepo.record(new AuditEntry(COMMAND, candidate, version, host, agent, platform));
                        ctx.redirect(302, result.get());
                    } else {
                        ctx.clientError(404);
                    }
                });
    }
}
