package io.sdkman.broker.binary;

import io.sdkman.broker.db.MongoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ApplicationRepo {

    private static final String APPLICATION_COLLECTION = "application";
    private static final String STABLE_VERSION_FIELD = "stableCliVersion";
    private static final String BETA_VERSION_FIELD = "betaCliVersion";

    private final static Logger LOG = LoggerFactory.getLogger(ApplicationRepo.class);

    private MongoProvider mongoProvider;

    @Inject
    public ApplicationRepo(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    public Promise<String> findVersion(final String versionType) {
        LOG.info("Finding the version for " + versionType);
        return Blocking.get(() ->
                mongoProvider
                        .database()
                        .getCollection(APPLICATION_COLLECTION)
                        .find()
                        .first()
                        .getString(cliVersionField(versionType)));
    }

    private String cliVersionField(String typeIdentifier) {
        switch (typeIdentifier) {
            case "stable":
                return STABLE_VERSION_FIELD;
            case "beta":
                return BETA_VERSION_FIELD;
            default:
                return "";
        }
    }
}