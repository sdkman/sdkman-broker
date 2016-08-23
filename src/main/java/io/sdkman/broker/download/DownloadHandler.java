package io.sdkman.broker.download;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.path.PathTokens;

@Singleton
public class DownloadHandler implements Handler {

    private final static Logger LOG = LoggerFactory.getLogger(DownloadHandler.class);

    private DownloadResolver downloadResolver;

    @Inject
    public DownloadHandler(DownloadResolver downloadResolver) {
        this.downloadResolver = downloadResolver;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        PathTokens pathTokens = ctx.getAllPathTokens();
        String candidate = pathTokens.get("candidate");
        String version = pathTokens.get("version");
        LOG.info("Received download request for: " + candidate + " " + version);
        downloadResolver.download(candidate, version)
                .then(result -> {
                    if (result.isPresent())
                        ctx.redirect(302, result.get());
                    else
                        ctx.clientError(404);
                });
    }
}
