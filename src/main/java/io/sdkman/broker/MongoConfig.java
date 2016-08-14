package io.sdkman.broker;

public class MongoConfig {
    private String host = "mongo";
    private int port = 27017;
    private String username = null;
    private String password = null;
    private String dbName = "sdkman";

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDbName() {
        return dbName;
    }
}
