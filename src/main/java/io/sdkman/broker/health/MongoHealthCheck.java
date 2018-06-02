package io.sdkman.broker.health;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sdkman.broker.app.AppRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.health.HealthCheck;
import ratpack.registry.Registry;

@Singleton
public class MongoHealthCheck implements HealthCheck {

    public static final String UNHEALTHY_MESSAGE = "Nothing found at application/alive in database.";

    private static final Logger logger = LoggerFactory.getLogger(MongoHealthCheck.class);

    private final AppRepo appRepo;

    @Inject
    public MongoHealthCheck(AppRepo appRepo) {
        this.appRepo = appRepo;
    }

    @Override
    public String getName() {
        return "alive";
    }

    @Override
    public Promise<Result> check(Registry registry) throws Exception {
        logger.info("Healthcheck request received.");
        return appRepo.healthCheck()
                .map(os -> os
                        .map(Result::healthy)
                        .orElse(Result.unhealthy(UNHEALTHY_MESSAGE)));
    }
}
