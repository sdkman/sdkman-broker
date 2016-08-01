package io.sdkman.broker;

import ratpack.guice.Guice;
import ratpack.server.RatpackServer;

import static ratpack.jackson.Jackson.json;

public class Main {
    public static void main(String[] args) throws Exception {
        RatpackServer.start(spec -> spec
                .registry(Guice.registry(g -> g
                        .bind(MongoProvider.class)
                        .bind(HealthRepo.class)))
                .handlers(chain -> chain
                        .get("alive", ctx -> {
                            HealthRepo healthRepo = ctx.get(HealthRepo.class);
                            ctx.render(json(healthRepo.isAlive()));
                        })));
    }
}
