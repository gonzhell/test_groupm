package com.groupm.demo.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class TunnelData {
    private static final Logger logger = LogManager.getLogger(TunnelData.class);

    private String tunnelHost;
    private String tunnelUser;
    private Integer tunnelPort;
    private String privateKeyFile;
    private String remoteHost;
    private Integer remotePort;
    private Integer localPort;

    // Default constructor
    public TunnelData(String tunnelHost, String tunnelUser, String privateKeyFile, String remoteHost, int remotePort, int localPort) {
        // TODO: Add integrity checks
        this.tunnelHost = tunnelHost;
        this.tunnelUser = tunnelUser;
        this.tunnelPort = 22;

        File key = new File(privateKeyFile);
        if (key.exists() && key.canRead()) {
            this.privateKeyFile = privateKeyFile;
        } else {
            logger.error("Private key file not found or inaccessible: {}", privateKeyFile);
            throw new IllegalArgumentException("Cannot access default key");
        }

        this.remoteHost = remoteHost;

        if (validatePort(remotePort)) {
            this.remotePort = remotePort;
        }
        if (validatePort(localPort)) {
            this.localPort = localPort;
        }
    }

    public TunnelData(String tunnelHost, String tunnelUser, String privateKey, String remoteHost) {
        this(tunnelHost, tunnelUser, privateKey, remoteHost, 5432, 4082);
    }

    public TunnelData(String tunnelHost, String remoteHost, int localPort) {
        this(tunnelHost, getEnvVar("TUNNEL_USER"), getEnvVar("PRIVATE_KEY"), remoteHost, 5432, localPort);
    }

    // TODO: Add more construstors with default values;

    // Getters
    public String getTunnelHost() {
        return this.tunnelHost;
    }

    public String getTunnelUser() {
        return this.tunnelUser;
    }

    public Integer getTunnelPort() { return this.tunnelPort; }

    public String getPrivateKeyFile() {
        return this.privateKeyFile;
    }

    public String getRemoteHost() {
        return this.remoteHost;
    }

    public Integer getRemotePort() {
        return this.remotePort;
    }

    public Integer getLocalPort() {
        return this.localPort;
    }

    private boolean validatePort (Integer port) {
        if (port < 1 && port > 65535) {
            logger.error("Port must be between 1 and 65535.");
            throw new IllegalArgumentException(String.format("Invalid port value: %d", port));
        } else if (port < 1025) {
            logger.warn("You chose one of the system reserved ports");
        }
        return true;
    }

    // function to check if value was correctly set from OS environment variable
    private static String getEnvVar(String varName) {
        String envVar = System.getenv(varName);
        if (envVar != null && envVar.length() > 0) {
            return envVar;
        } else {
            throw new IllegalArgumentException(String.format("Enviroment variable '%s' is not set", varName));
        }
    }
}
