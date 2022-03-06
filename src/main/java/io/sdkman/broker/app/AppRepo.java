package io.sdkman.broker.app;

import io.sdkman.broker.db.MongoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Singleton
public class AppRepo {

    private static final String APPLICATION_COLLECTION_NAME = "application";
    private static final String ALIVE_FIELD_NAME = "alive";
    private static final String ALIVE_FIELD_VALUE = "OK";
    private static final String STABLE_VERSION_FIELD_NAME = "stableCliVersion";
    private static final String BETA_VERSION_FIELD_NAME = "betaCliVersion";
    private static final String STABLE_NATIVE_VERSION_FIELD_NAME = "stableNativeCliVersion";

    private final static Logger LOG = LoggerFactory.getLogger(AppRepo.class);

    private MongoProvider mongoProvider;

    @Inject
    public AppRepo(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    public Promise<Optional<String>> healthCheck() {
        return Blocking.get(() ->
                Optional.ofNullable(
                        mongoProvider.database()
                                .getCollection(APPLICATION_COLLECTION_NAME)
                                .find(eq(ALIVE_FIELD_NAME, ALIVE_FIELD_VALUE))
                                .first()
                                .getString(ALIVE_FIELD_NAME)));
    }

    public Promise<String> findVersion(final String versionType) {
        LOG.info("Finding the version for " + versionType);
        return Blocking.get(() ->
                mongoProvider
                        .database()
                        .getCollection(APPLICATION_COLLECTION_NAME)
                        .find()
                        .first()
                        .getString(cliVersionField(versionType)));
    }

    private String cliVersionField(String typeIdentifier) {
        switch (typeIdentifier) {
            case "stable":
                return STABLE_VERSION_FIELD_NAME;
            case "stable_native":
                return STABLE_NATIVE_VERSION_FIELD_NAME;
            case "beta":
                return BETA_VERSION_FIELD_NAME;
            default:
                return "";
        }
    }
}