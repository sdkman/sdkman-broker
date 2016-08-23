package io.sdkman.broker.download;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.client.MongoDatabase;
import io.sdkman.broker.db.MongoProvider;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import java.util.Optional;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Singleton
public class DownloadResolver {

    public static final String COLLECTION_NAME = "versions";
    public static final String CANDIDATE_FIELD = "candidate";
    public static final String VERSION_FIELD = "version";
    private final MongoProvider mongoProvider;

    @Inject
    public DownloadResolver(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    public Promise<Optional<String>> download(String candidate, String version) throws Exception {
        return Blocking.get(() -> {
            MongoDatabase mongo = mongoProvider.database();
            return Optional.of(mongo.getCollection(COLLECTION_NAME))
                    .map(coll -> coll.find(and(eq(CANDIDATE_FIELD, candidate), eq(VERSION_FIELD, version))).first())
                    .map(doc -> doc.getString("url"));
        });
    }
}
