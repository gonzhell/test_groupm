package com.groupm.demo;

import com.groupm.demo.db.DBData;
import com.groupm.demo.db.PostgreConnector2;
import com.groupm.demo.db.TunnelData;
import com.jcraft.jsch.JSchException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qaprosoft.carina.core.foundation.AbstractTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DB extends AbstractTest {
    static PostgreConnector2 db_conn;

    @BeforeClass
    public static void createConnections() throws JSchException {
        if (db_conn == null) {
            DBData oldDbData = new DBData("localhost", 5432, "test", "test", "Test1");
            db_conn = new PostgreConnector2(oldDbData);
        }
        db_conn.openConnection();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        try {
            db_conn.closeConnection().closeTunnel();
        } catch (Exception ignored) {
        }
    }

    @Parameters({"query", "result"})
    @Test
    public void TestCreatives(String query, String result) throws SQLException {
        ArrayList<Map<String, String>> res = db_conn.query(query);
        Map<String, String> map = res.get(0);
        Assert.assertEquals(result, map.get("res"), "There is wrong data in db");

    }
}
