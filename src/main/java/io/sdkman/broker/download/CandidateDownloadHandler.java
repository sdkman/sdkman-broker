package io.sdkman.broker.download;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.version.VersionRepo;
import io.sdkman.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Request;
import ratpack.path.PathTokens;
import ratpack.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CandidateDownloadHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(CandidateDownloadHandler.class);

    private static final String COMMAND = "install";

    private final VersionRepo versionRepo;
    private final AuditRepo auditRepo;
    private final DownloadResolver downloadResolver;

    @Inject
    public CandidateDownloadHandler(VersionRepo versionRepo, AuditRepo auditRepo, DownloadResolver downloadResolver) {
        this.versionRepo = versionRepo;
        this.auditRepo = auditRepo;
        this.downloadResolver = downloadResolver;
    }

    @Override
    public void handle(Context ctx) {
        RequestDetails.of(ctx).ifPresentOrElse(details -> {
            logger.info("Received download request for: {}", details);
            Platform.of(details.getPlatform())
                    .ifPresentOrElse(p -> versionRepo
                                    .fetch(details.getCandidate(), details.getVersion())
                                    .then((List<Version> downloads) ->
                                            downloadResolver.resolve(downloads, p.name())
                                                    .ifPresentOrElse(v -> {
                                                        audit(details, p.id(), v.platform());
                                                        ctx.redirect(302, v.url());
                                                    }, clientError(ctx, 404))),
                            clientError(ctx, 400));
        }, clientError(ctx, 404));
    }

    private Runnable clientError(Context ctx, int code) {
        return () -> ctx.clientError(code);
    }

    private void audit(RequestDetails details, String platform, String dist) {
        auditRepo.record(
                AuditEntry.of(
                        COMMAND, details.getCandidate(), details.getVersion(), details.getHost(),
                        details.getAgent(), platform, dist));
    }

    public static class RequestDetails {

        private static final String PLATFORM_PARAMETER_NAME = "platform";
        private static final String CANDIDATE_PARAMETER_NAME = "candidate";
        private static final String VERSION_PARAMETER_NAME = "version";

        private final String candidate;
        private final String version;
        private final String host;
        private final String agent;
        private final String platform;

        private RequestDetails(String candidate, String version, String host, String agent, String platform) {
            this.candidate = candidate;
            this.version = version;
            this.host = host;
            this.agent = agent;
            this.platform = platform;
        }

        public static Optional<RequestDetails> of(Context ctx) {
            PathTokens pathTokens = ctx.getAllPathTokens();
            Request request = ctx.getRequest();
            if (isValidRequest(ctx)) {
                return Optional.of(
                        new RequestDetails(
                                pathTokens.get(CANDIDATE_PARAMETER_NAME),
                                pathTokens.get(VERSION_PARAMETER_NAME),
                                request.getHeaders().get("X-Real-IP"),
                                request.getHeaders().get("user-agent"),
                                determineNormalisedPlatform(pathTokens, request)));
            } else {
                return Optional.empty();
            }
        }

        private static boolean isValidRequest(Context ctx) {
            PathTokens pathTokens = ctx.getAllPathTokens();
            MultiValueMap<String, String> queryParams = ctx.getRequest().getQueryParams();
            return isParameterPresentIn(pathTokens, CANDIDATE_PARAMETER_NAME) &&
                    isParameterPresentIn(pathTokens, VERSION_PARAMETER_NAME) &&
                    isParameterPresentIn(pathTokens, PLATFORM_PARAMETER_NAME) ||
                    isParameterPresentIn(queryParams, PLATFORM_PARAMETER_NAME);
        }

        private static String determineNormalisedPlatform(PathTokens pathTokens, Request request) {
            return isParameterPresentIn(pathTokens, PLATFORM_PARAMETER_NAME) ?
                    pathTokens.get(PLATFORM_PARAMETER_NAME) :
                    request.getQueryParams().get(PLATFORM_PARAMETER_NAME).toLowerCase();
        }

        private static boolean isParameterPresentIn(Map<String, String> map, String key) {
            return map.get(key) != null;
        }

        public String getCandidate() {
            return candidate;
        }

        public String getVersion() {
            return version;
        }

        public String getHost() {
            return host;
        }

        public String getAgent() {
            return agent;
        }

        public String getPlatform() {
            return platform;
        }

        @Override
        public String toString() {
            return "RequestDetails{" +
                    "candidate='" + candidate + '\'' +
                    ", version='" + version + '\'' +
                    ", host='" + host + '\'' +
                    ", agent='" + agent + '\'' +
                    ", platform='" + platform + '\'' +
                    '}';
        }
    }
}
