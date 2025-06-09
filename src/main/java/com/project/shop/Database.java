package com.project.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public String url = "jdbc:mysql://localhost";
    public Connection connection;

    public Database(String dbName, String dbPassword, int port) {
        String fullJdbcUrl = String.format("%s:%d/%s", this.url, port, dbName);

        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(fullJdbcUrl, "root", dbPassword);

            if (connection != null) {
                System.out.println("Successfully connected to the database!");
                this.connection = connection;
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            AppConfig.connection = null;
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            AppConfig.connection = null;
        }
    }
}