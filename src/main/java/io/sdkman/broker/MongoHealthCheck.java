package io.sdkman.broker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.health.HealthCheck;
import ratpack.registry.Registry;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Singleton
class MongoHealthCheck implements HealthCheck {
    private static final String COLLECTION_NAME = "application";
    private static final String FIELD_NAME = "alive";
    private static final String FIELD_VALUE = "OK";
    private static final String UNHEALTHY_MESSAGE = "null value returned from application/alive in mongodb.";

    private final MongoProvider mongoProvider;

    @Inject
    public MongoHealthCheck(MongoProvider mongoProvider) {
        this.mongoProvider = mongoProvider;
    }

    @Override
    public String getName() {
        return "alive";
    }

    @Override
    public Promise<Result> check(Registry registry) throws Exception {
        MongoDatabase mongo = mongoProvider.database();
        return Blocking.get(() -> {
            MongoCollection<Document> collection = mongo.getCollection(COLLECTION_NAME);
            return Optional.ofNullable(collection.find(eq(FIELD_NAME, FIELD_VALUE)).first())
                    .filter(first -> first.getString(FIELD_NAME).equals(FIELD_VALUE))
                    .map(r -> HealthCheck.Result.healthy())
                    .orElse(HealthCheck.Result.unhealthy(UNHEALTHY_MESSAGE));
        });
    }
}
