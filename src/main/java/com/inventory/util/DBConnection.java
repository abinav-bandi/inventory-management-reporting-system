package com.inventory.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    private static boolean testMode = false;

    public static void enableTestMode() {
        testMode = true;
    }

    public static Connection getConnection() {
        String url = System.getenv("DBURL");
        String user = System.getenv("USERNAME");
        String password = System.getenv("PASSWORD");


        if ((url == null || user == null || password == null) && testMode) {
            Dotenv dotenv = Dotenv.load();
            url = dotenv.get("DBURL");
            user = dotenv.get("USERNAME");
            password = dotenv.get("PASSWORD");
        }

        if (url == null || user == null || password == null) {
            throw new RuntimeException("❌ Database environment variables are not set or .env not found");
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("❌ Database connection failed: " + e.getMessage(), e);
        }
    }
}
