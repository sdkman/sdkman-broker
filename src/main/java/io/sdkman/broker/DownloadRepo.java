package io.sdkman.broker;

import com.google.inject.Inject;
import ratpack.exec.Promise;

import java.net.URL;

class DownloadRepo {

    private MongoConnectionProvider mongoConnectionProvider;

    @Inject
    public DownloadRepo(MongoConnectionProvider mongoConnectionProvider) {
        this.mongoConnectionProvider = mongoConnectionProvider;
    }

    Promise<URL> downloadUrl(String candidate, String version) {
        return Promise.async(downstream -> {
            mongoConnectionProvider.connection();
        });
    }

}
