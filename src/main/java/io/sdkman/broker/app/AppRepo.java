package io.sdkman.broker.app;

import io.sdkman.broker.db.MongoProvider;
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
    private static final String STABLE_BASH_VERSION_FIELD_NAME = "stableCliVersion";
    private static final String BETA_BASH_VERSION_FIELD_NAME = "betaCliVersion";
    private static final String STABLE_NATIVE_VERSION_FIELD_NAME = "stableNativeCliVersion";
    private static final String BETA_NATIVE_VERSION_FIELD_NAME = "betaNativeCliVersion";

    private final MongoProvider mongoProvider;

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

    public Promise<String> findVersion(final String impl, final String channel) {
        return Blocking.get(() ->
                mongoProvider
                        .database()
                        .getCollection(APPLICATION_COLLECTION_NAME)
                        .find()
                        .first()
                        .getString(cliVersionField(channel, impl)));
    }

    private String cliVersionField(final String channel, final String impl) {
        switch (channel) {
            case "stable":
                if (impl.equals("native")) {
                    return STABLE_NATIVE_VERSION_FIELD_NAME;
                }
                return STABLE_BASH_VERSION_FIELD_NAME;
            case "beta":
                if (impl.equals("native")) {
                    return BETA_NATIVE_VERSION_FIELD_NAME;
                }
                return BETA_BASH_VERSION_FIELD_NAME;
            default:
                return "";
        }
    }
}