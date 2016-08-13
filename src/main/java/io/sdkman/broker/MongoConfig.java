package io.sdkman.broker;

public class MongoConfig {
    private String mongoHost = "mongo";
    private int mongoPort = 27017;
    private String mongoUsername = null;
    private String mongoPassword = null;
    private String mongoDbName = "sdkman";

    public String getMongoHost() {
        return mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public String getMongoUsername() {
        return mongoUsername;
    }

    public String getMongoPassword() {
        return mongoPassword;
    }

    public String getMongoDbName() {
        return mongoDbName;
    }
}
