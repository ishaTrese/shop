package com.project.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.project.shop.Database;

@SpringBootApplication
public class ShopApplication {
    public static void main(String[] args) {
        try {
            System.out.println("Connecting to database...");
            Database db = new Database("shop", "password", 3306);
            AppConfig.connection = db.connection;
            
            if (AppConfig.connection != null) {
                System.out.println("Database connection successful!");
                System.out.println("Initializing database tables and products...");
                AppConfig.initializeDatabase();
                System.out.println("Database initialization completed!");
            } else {
                System.err.println("ERROR: Database connection failed!");
                System.err.println("Please check your database configuration:");
                System.err.println("- Database name: shop");
                System.err.println("- Username: root");
                System.err.println("- Password: password");
                System.err.println("- Port: 3306");
                System.err.println("- Make sure MySQL is running");
            }
        } catch (Exception e) {
            System.err.println("ERROR during application startup: " + e.getMessage());
            e.printStackTrace();
        }
        
        SpringApplication.run(ShopApplication.class, args);
    }
}

