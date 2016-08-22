package io.sdkman.broker;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.health.HealthCheck;
import ratpack.registry.Registry;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

@Singleton
class MongoHealthCheck implements HealthCheck {

    private final static Logger LOG = LoggerFactory.getLogger(MongoHealthCheck.class);

    public static final String COLLECTION_NAME = "application";
    public static final String FIELD_NAME = "alive";
    public static final String FIELD_VALUE = "OK";
    public static final String UNHEALTHY_MESSAGE = "Nothing found at application/alive in database.";

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
            Result result = Optional.ofNullable(collection.find(eq(FIELD_NAME, FIELD_VALUE)).first())
                    .filter(first -> FIELD_VALUE.equals(first.getString(FIELD_NAME)))
                    .map(r -> Result.healthy())
                    .orElse(Result.unhealthy(UNHEALTHY_MESSAGE));
            LOG.info("Health check performed - healthy: " + result.isHealthy() + ", message: " + result.getMessage());
            return result;
        });
    }

}
