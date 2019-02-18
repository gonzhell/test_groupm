package com.groupm.demo.db;

import com.jcraft.jsch.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aaduevsky on 19-Mar-17.
 * Usage sample:
 * PostgreConnector connector = new PostgreConnector("localhost", "tdapi_devdb", "tdapi_devdb", "%N80TpR,ZZ&Q>qa");
 * connector.tunnel("35.160.158.184", "esicinskaia", "tdapi-dev.cbmcgnabqe7k.us-west-2.rds.amazonaws.com");
 * connector.connect();
 * <p>
 * String query = "SELECT table_name FROM information_schema.tables";
 * List<Map<String, String>> data = connector.query(query);
 * <p>
 * System.out.println("total rows: " + data.size());
 * System.out.println("Row 1 data:");
 * for (Map.Entry entry : data.get(1).entrySet()) {
 * System.out.println(entry.getKey() + ": " + entry.getValue());
 * }
 * <p>
 * connector.closeConnection();
 * connector.closeTunnel();
 */
public class PostgreConnector2 extends AbstractDB {

    private static final Logger logger = LogManager.getLogger(PostgreConnector2.class);

    private Connection connection = null;
    private Session session = null;

    private int assignedPort = -1;

    public PostgreConnector2(DBData dbConfig) {
        super(dbConfig);
    }

    public PostgreConnector2(DBData dbConfig, TunnelData tunnelConfig) {
        super(dbConfig, tunnelConfig);
    }

    public PostgreConnector2 openTunnel() {
        if (this.session != null && this.session.isConnected()) {
            logger.warn("Connection is already open.");
            return this;
        }
        if (System.getenv("TUNNEL") != null && System.getenv("tunnel").equalsIgnoreCase("no")) {
            logger.warn("Tunnel won't be created as per user request");
            assignedPort = dbConfig.getPort();
        } else {
            JSch jsch = new JSch();

            try {
                session = jsch.getSession(tunnelConfig.getTunnelUser(), tunnelConfig.getTunnelHost(), tunnelConfig.getTunnelPort());
                jsch.addIdentity(tunnelConfig.getPrivateKeyFile());

                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                config.put("Compression", "yes");
                config.put("ConnectionAttempts", "2");

                session.setConfig(config);
                assignedPort = session.setPortForwardingL(tunnelConfig.getLocalPort(), tunnelConfig.getRemoteHost(), tunnelConfig.getRemotePort());

                // Connect
                session.connect();
                session.openChannel("direct-tcpip");
            } catch (Exception ex) {
                logger.error("Tunnel connection attempt failed.", ex);
                session.disconnect();
                session = null;
            }
        }
        return this;
    }

    public PostgreConnector2 openConnection() {
        // TODO: check if no tunnel

        // Build the  database connection URL.
        StringBuilder url = new StringBuilder("jdbc:postgresql://" + dbConfig.getHost() + ":");

        // use assigned_port to establish database connection
        if (assignedPort == -1 ){
            logger.warn("Assigned port not set; using DB config port");
            assignedPort = dbConfig.getPort();
        }
        url.append(assignedPort).append("/").append(dbConfig.getDbName());
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Cannot find postgre driver", e);
            connection = null;
            return this;
        }
        logger.trace("Database connection URL: {}", url);

        try {
            connection = DriverManager.getConnection(url.toString(), dbConfig.getDbUser(), dbConfig.getDbPassword());

        } catch (SQLException e) {
            logger.error("Connection Failed! ", e);
            connection = null;
            return this;
        }

        if (connection != null) {
            logger.debug("You made it, take control your database now!");
        } else {
            logger.error("Failed to make connection!");
            connection = null;
            return this;
        }
        return this;
    }

    public ArrayList<Map<String, String>> query(String queryStr) throws SQLException {
        // TODO: if no connection connect to db
        if (!connectionIsOpen()) openConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(queryStr);
        ResultSetMetaData rsmd = rs.getMetaData();
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            columnNames.add(rsmd.getColumnLabel(i));
        }
        ArrayList<Map<String, String>> data = new ArrayList<>();
        while (rs.next()) {
            Map<String, String> rowData = new HashMap<>();
            for (String columnName : columnNames) {
                if (rs.getObject(columnName) == null) rowData.put(columnName, "Null");
                else rowData.put(columnName, rs.getObject(columnName).toString());
            }
            data.add(rowData);
        }
        rs.close();
        return data;
    }

    public PostgreConnector2 closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Problem closing connection to db", e);
            connection = null;
        }
        return this;
    }

    public PostgreConnector2 closeTunnel() {
        session.disconnect();
        session = null;
        return this;
    }

    public boolean tunnelIsOpen() {
        try {
            return session.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean connectionIsOpen() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public String openTextFile(String filePath) throws JSchException, SftpException, IOException {
        ChannelSftp sftp = (ChannelSftp) this.session.openChannel("sftp");
        InputStream stream = sftp.get(filePath);
        //ArrayList<String> fileLines = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String currLine = br.readLine();
            while (currLine != null) {
                sb.append(currLine);
            }
        } finally {
            stream.close();
        }
        return sb.toString();
    }

}
