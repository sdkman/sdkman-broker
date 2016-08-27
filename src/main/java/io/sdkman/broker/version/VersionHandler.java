package io.sdkman.broker.version;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import static ratpack.jackson.Jackson.json;

@Singleton
public class VersionHandler implements Handler {
    private final VersionConfig versionConfig;

    @Inject
    public VersionHandler(VersionConfig versionConfig) {
        this.versionConfig = versionConfig;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        ctx.render(json(versionConfig));
    }
}
