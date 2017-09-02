package io.sdkman.broker.binary;

import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
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
        String version = "5.5.11+256";
        String binaryUrl = config.getProtocol() + "://" + config.getHost() + config.getUri() + config.getName();
        LOG.info(binaryUrl);
        String formattedUrl = String.format(binaryUrl, version);
        LOG.info(formattedUrl);
        ctx.redirect(formattedUrl);
        auditRepo.record(new AuditEntry("install", "sdkman", "5.5.11+256", "host", "agent", "Darwin", "UNIVERSAL"));
    }
}
