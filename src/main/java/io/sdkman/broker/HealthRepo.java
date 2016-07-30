package io.sdkman.broker;

import com.google.inject.Inject;

class HealthRepo {
    private final MongoConnectionProvider mongoConnectionProvider;

    @Inject
    public HealthRepo(MongoConnectionProvider mongoConnectionProvider) {
        this.mongoConnectionProvider = mongoConnectionProvider;
    }

    HealthResponse isAlive() {
        mongoConnectionProvider.connection();
        return new HealthResponse("OK");
    }
}
