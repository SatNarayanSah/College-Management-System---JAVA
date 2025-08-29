package com.cms.dao;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "/config/database.properties";
    private static Connection connection;
    
    // Database credentials (you can move these to properties file)
    private static final String URL = "jdbc:mysql://localhost:3306/college_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password"; // Change this
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
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
