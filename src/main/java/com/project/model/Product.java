package com.project.model;

import java.math.BigDecimal;

public class Product {
    private Long id;
    private String name;
    private String description;
    private String category;
    private int stock;
    private BigDecimal price;
    private String imageUrl;

    public Product() {}

    public Product(Long id, String name, String description, String category, int stock, BigDecimal price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, String description, String category, int stock, BigDecimal price, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
