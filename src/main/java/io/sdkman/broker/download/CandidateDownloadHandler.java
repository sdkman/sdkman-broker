package io.sdkman.broker.download;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sdkman.broker.audit.AuditEntry;
import io.sdkman.broker.audit.AuditRepo;
import io.sdkman.broker.version.VersionRepo;
import io.sdkman.model.MD5;
import io.sdkman.model.SHA1;
import io.sdkman.model.SHA224;
import io.sdkman.model.SHA256;
import io.sdkman.model.SHA384;
import io.sdkman.model.SHA512;
import io.sdkman.model.Version;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.Request;
import ratpack.path.PathTokens;
import ratpack.util.MultiValueMap;
import scala.Unit;
import scala.collection.JavaConverters;

@Singleton
public class CandidateDownloadHandler implements Handler {

    private static final String COMMAND = "install";
    private static final String X_SDK_MAN_CHECKSUM = "X-SdkMan-Checksum";
    private static final Logger logger = LoggerFactory.getLogger(CandidateDownloadHandler.class);
    private static final Comparator<String> algoComparator =
            Comparator.comparing((Function<String, Integer>) algorithm -> {
                if (MD5.id().equals(algorithm)) {
                    return MD5.priority();
                } else if (SHA1.id().equals(algorithm)) {
                    return SHA1.priority();
                } else if (SHA224.id().equals(algorithm)) {
                    return SHA224.priority();
                } else if (SHA256.id().equals(algorithm)) {
                    return SHA256.priority();
                } else if (SHA384.id().equals(algorithm)) {
                    return SHA384.priority();
                } else if (SHA512.id().equals(algorithm)) {
                    return SHA512.priority();
                } else {
                    return 0;
                }
            }).reversed();


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
                                                        v.checksums().foreach(v1 ->
                                                                writeChecksumHeaders(ctx, v1));

                                                        ctx.redirect(302, v.url());
                                                    }, clientError(ctx, 404))),
                            clientError(ctx, 400));
        }, clientError(ctx, 404));
    }

    private Unit writeChecksumHeaders(
            Context ctx,
            scala.collection.immutable.Map<String, String> checksums) {

        Map<String, String> sortedChecksums = new TreeMap<>(algoComparator);
        sortedChecksums.putAll(JavaConverters.mapAsJavaMap(checksums));

        sortedChecksums.forEach((algo, checksum) ->
                ctx.header(String.format("%s-%s", X_SDK_MAN_CHECKSUM, algo), checksum));
        return null;
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
