package io.sdkman.broker.download;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sdkman.broker.db.MongoProvider;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Singleton
public class VersionRepo {

    public static final String COLLECTION_NAME = "versions";
    public static final String CANDIDATE_FIELD = "candidate";
    public static final String VERSION_FIELD = "version";
    private final MongoProvider mongoProvider;

    @Inject
    public VersionRepo(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    public Promise<List<Version>> fetchDownloads(String candidate, String version) {
        return Blocking.get(() -> newArrayList(
                mongoProvider
                        .database()
                        .getCollection(COLLECTION_NAME)
                        .find(and(eq(CANDIDATE_FIELD, candidate), eq(VERSION_FIELD, version)))
                        .map(doc -> new Version(
                                candidate,
                                version,
                                doc.getString("url"),
                                doc.getString("platform")))));
    }
}
