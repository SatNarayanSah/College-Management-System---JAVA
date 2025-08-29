package com.cms.dao;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "/database.properties";
    private static Connection connection;
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String DRIVER;

    // Static block to load properties at class initialization
    static {
        try {
            // Load properties file from classpath
            Properties props = new Properties();
            // Try multiple paths to find the properties file
            InputStream inputStream = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE);
            if (inputStream == null) {
                inputStream = DatabaseConnection.class.getResourceAsStream("/config" + PROPERTIES_FILE);
            }
            if (inputStream == null) {
                inputStream = ClassLoader.getSystemResourceAsStream("config/database.properties");
            }
            if (inputStream == null) {
                throw new IllegalStateException("Properties file not found. Tried: " + PROPERTIES_FILE + " and /config" + PROPERTIES_FILE);
            }
            props.load(inputStream);
            inputStream.close();

            // Read properties
            URL = props.getProperty("db.url");
            USERNAME = props.getProperty("db.username");
            PASSWORD = props.getProperty("db.password");
            DRIVER = props.getProperty("db.driver");

            // Validate properties
            if (URL == null || USERNAME == null || PASSWORD == null || DRIVER == null) {
                throw new IllegalStateException("Missing required database properties");
            }

            // Load JDBC driver
            Class.forName(DRIVER);
        } catch (Exception e) {
            System.err.println("Failed to load database properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}