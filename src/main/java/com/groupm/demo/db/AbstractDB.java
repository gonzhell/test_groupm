package com.groupm.demo.db;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractDB {
    protected DBData dbConfig;
    protected TunnelData tunnelConfig;

    public AbstractDB (DBData dbConfig) {
        this.dbConfig = dbConfig;
    }

    public AbstractDB (DBData dbConfig, TunnelData tunnelConfig) {
        this.dbConfig = dbConfig;
        this.tunnelConfig = tunnelConfig;
    }

    public abstract AbstractDB openTunnel(); //TODO: remove and check from connect();

    public abstract AbstractDB openConnection(); //TODO: remove and check from query();

    public abstract ArrayList<Map<String,String>> query(String queryStr) throws SQLException;

    public abstract AbstractDB closeConnection();

    public abstract AbstractDB closeTunnel();

    public abstract boolean tunnelIsOpen();

    public abstract boolean connectionIsOpen();

    public abstract String openTextFile(String filePath) throws JSchException, SftpException, IOException;
}
