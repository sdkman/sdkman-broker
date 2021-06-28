package io.sdkman.broker.db;

public class MongoConfig {
    private String host = "localhost";
    private int port = 27017;
    private String username = null;
    private String password = null;
    private String dbName = "sdkman";
    private int serverSelectionTimeout = 1000 * 5;
    private int connectionTimeout = 1000 * 5;
    private int socketTimeout = 0;

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

    public int getServerSelectionTimeout() {
        return serverSelectionTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }
}
