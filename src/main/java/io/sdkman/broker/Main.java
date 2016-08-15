package io.sdkman.broker;

import ratpack.guice.Guice;
import ratpack.server.RatpackServer;

import static ratpack.jackson.Jackson.json;

public class Main {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(spec -> spec
                .serverConfig(c -> c
                        .env()
                        .require("/mongo", MongoConfig.class)
                )
                .registry(Guice.registry(g -> g
                        .bind(MongoProvider.class)
                        .bind(HealthRepo.class)))
                .handlers(chain -> chain
                        .get("alive", ctx -> {
                            HealthRepo healthRepo = ctx.get(HealthRepo.class);
                            healthRepo.isAlive().then(healthResponse ->
                                    ctx.render(json(healthResponse)));
                        })));
    }
}
