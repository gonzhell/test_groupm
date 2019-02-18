package com.groupm.demo.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DBData {

    private static final Logger logger = LogManager.getLogger(DBData.class);

    private String host;
    private Integer port;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    // Default constructor
    public DBData(String host, int port, String dbName, String dbUser, String dbPassword) {
        if (port < 1 && port > 65535) {
            logger.error("Port must be between 1 and 65535.");
            throw new IllegalArgumentException(String.format("Invalid port value: %d", port));
        } else if (port < 1025) {
            logger.warn("You chose one of the system reserved ports");
        }
        // TODO: Add more integrity checks

        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public DBData(String host, String dbName, String dbUser, String dbPassword) {
        this(host, 5432, dbName, dbUser, dbPassword);
    }
    // TODO: Add more construstors with default values;

    // Getters
    public String getHost() {
        return this.host;
    }

    public Integer getPort() {
        return this.port;
    }

    public String getDbName() {
        return this.dbName;
    }

    public String getDbUser() {
        return this.dbUser;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }
}
