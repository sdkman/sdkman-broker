package io.sdkman.broker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import static com.mongodb.client.model.Filters.eq;

@Singleton
class HealthRepo {
    private static final String COLLECTION_NAME = "application";
    private static final String FIELD_NAME = "alive";

    private final MongoProvider mongoProvider;

    @Inject
    public HealthRepo(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    Promise<HealthResponse> isAlive() throws Exception {
        MongoDatabase mongo = mongoProvider.database();
        return Blocking.get(() -> {
            MongoCollection<Document> collection = mongo.getCollection(COLLECTION_NAME);
            Document first = collection.find(eq(FIELD_NAME, "OK")).first();
            String alive = (String) first.get(FIELD_NAME);
            return new HealthResponse(alive);
        });
    }
}
