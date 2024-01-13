package com.vaguestudios.bot.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector {
    public static Connection connect() {
        String url = "jdbc:sqlite:src/main/java/com/vaguestudios/bot/database/vaguestudios.db";  // Path to your .db file
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
