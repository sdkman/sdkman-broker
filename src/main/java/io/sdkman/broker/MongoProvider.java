package io.sdkman.broker;

import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;

@Singleton
class MongoProvider {

    private final String mongoHost = "mongo";
    private final int mongoPort = 27017;
    private final String mongoUsername = null;
    private final String mongoPassword = null;
    private final String mongoDbName = "sdkman";

    public MongoDatabase database() throws Exception {
        return mongo().getDatabase(mongoDbName);
    }

    private MongoClient mongo() throws Exception {
        ServerAddress serverAddress = new ServerAddress(mongoHost, mongoPort);
        if (mongoUsername != null && mongoPassword != null) {
            MongoCredential credential = MongoCredential.createCredential(mongoUsername, mongoDbName, mongoPassword.toCharArray());
            List credentials = new ArrayList() {{
                add(credential);
            }};

            return new MongoClient(serverAddress, credentials);
        } else {
            return new MongoClient(serverAddress);
        }
    }
}
