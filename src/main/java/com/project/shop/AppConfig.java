package com.project.shop;

import java.sql.Connection;

public class AppConfig {
    public static Connection connection = null;
    public static int user = 0;

    public static String userName;
    public static String userAddress;
    public static String userEmail;
    public static String userPhone;

    public static int getCurrentUser() {
        return user;
    }

    public static void initializeDatabase() {
        if (connection != null) {
            createProductsTable();
            InventoryService inventoryService = new InventoryService();
            inventoryService.initializeProducts();
        }
    }

    private static void createProductsTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS products (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                description TEXT,
                category VARCHAR(100) NOT NULL,
                stock INT NOT NULL DEFAULT 0,
                price DECIMAL(10,2) NOT NULL,
                image_url VARCHAR(500),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            )
            """;
        
        try (var stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Products table created successfully!");
        } catch (Exception e) {
            System.err.println("Error creating products table: " + e.getMessage());
        }
    }
}
