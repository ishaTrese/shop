package com.project.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.project.shop.Database;

@SpringBootApplication
public class ShopApplication {
    public static void main(String[] args) {
        Database db = new Database("shop", "password", 3306);
        AppConfig.connection = db.connection;
        SpringApplication.run(ShopApplication.class, args);
    }
}

