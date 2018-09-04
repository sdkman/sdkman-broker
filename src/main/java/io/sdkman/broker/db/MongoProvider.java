package io.sdkman.broker.db;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MongoProvider {

    private final static Logger LOG = LoggerFactory.getLogger(MongoProvider.class);

    private final MongoConfig mongoConfig;
    private final MongoClient mongoClient;

    @Inject
    public MongoProvider(MongoConfig mongoConfig) {
        this.mongoConfig = mongoConfig;
        this.mongoClient = mongo();
    }

    private MongoClient mongo() {
        LOG.info("Initialising mongo client.");
        ServerAddress serverAddress = new ServerAddress(mongoConfig.getHost(), mongoConfig.getPort());
        MongoClientOptions clientOptions = MongoClientOptions.builder()
                .serverSelectionTimeout(mongoConfig.getServerSelectionTimeout())
                .connectTimeout(mongoConfig.getConnectionTimeout())
                .socketTimeout(mongoConfig.getSocketTimeout())
                .build();
        if (mongoConfig.getUsername() != null && mongoConfig.getPassword() != null) {
            MongoCredential credential = MongoCredential.createCredential(
                    mongoConfig.getUsername(),
                    mongoConfig.getDbName(),
                    mongoConfig.getPassword().toCharArray());
            List<MongoCredential> credentials = new ArrayList<MongoCredential>() {{
                add(credential);
            }};

            return new MongoClient(serverAddress, credentials, clientOptions);
        } else {
            return new MongoClient(serverAddress, clientOptions);
        }
    }

    public MongoDatabase database() throws Exception {
        return mongoClient.getDatabase(mongoConfig.getDbName());
    }
}
