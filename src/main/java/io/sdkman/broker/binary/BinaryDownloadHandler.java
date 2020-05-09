package io.sdkman.broker.binary;

import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.download.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Headers;
import ratpack.http.Request;
import ratpack.path.PathTokens;

import javax.inject.Inject;
import java.util.Optional;

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
    public void handle(Context ctx) throws Exception {
        RequestDetails.of(ctx).ifPresentOrElse(details -> {
            logger.info("Received download request for: " + details);
            record(details, inferPlatform(details.getPlatform()));
            ctx.redirect(String.format(prepareRemoteBinaryUrl(), details.getVersion()));
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

    public static class RequestDetails {

        private static final String COMMAND_TOKEN_NAME = "command";
        private static final String VERSION_TOKEN_NAME = "version";
        private static final String PLATFORM_TOKEN_NAME = "platform";
        private static final String HOST_HEADER_NAME = "X-Real-IP";
        private static final String AGENT_HEADER_NAME = "user-agent";

        private final String command;
        private final String version;
        private final String platform;
        private final String host;
        private final String agent;

        private RequestDetails(String command, String version, String platform, String host, String agent) {
            this.command = command;
            this.version = version;
            this.platform = platform;
            this.host = host;
            this.agent = agent;
        }

        public static Optional<RequestDetails> of(Context ctx) {
            Request request = ctx.getRequest();
            Headers headers = request.getHeaders();

            if (isValidRequest(ctx)) {
                PathTokens tokens = ctx.getAllPathTokens();
                return Optional.of(
                        new RequestDetails(
                                tokens.get(COMMAND_TOKEN_NAME),
                                tokens.get(VERSION_TOKEN_NAME),
                                tokens.get(PLATFORM_TOKEN_NAME),
                                headers.get(HOST_HEADER_NAME),
                                headers.get(AGENT_HEADER_NAME)));
            } else {
                return Optional.empty();
            }
        }

        private static boolean isValidRequest(Context ctx) {
            PathTokens tokens = ctx.getAllPathTokens();
            return tokens.get(COMMAND_TOKEN_NAME) != null
                    && tokens.get(VERSION_TOKEN_NAME) != null
                    && tokens.get(PLATFORM_TOKEN_NAME) != null;
        }

        public String getCommand() {
            return command;
        }

        public String getVersion() {
            return version;
        }

        public String getPlatform() {
            return platform;
        }

        public String getHost() {
            return host;
        }

        public String getAgent() {
            return agent;
        }

        @Override
        public String toString() {
            return "RequestDetails{" +
                    "command='" + command + '\'' +
                    ", version='" + version + '\'' +
                    ", platform='" + platform + '\'' +
                    ", host='" + host + '\'' +
                    ", agent='" + agent + '\'' +
                    '}';
        }
    }
}
