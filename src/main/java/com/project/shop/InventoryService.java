package com.project.shop;

import com.project.model.Product;
import com.project.model.ProductData;

import java.util.ArrayList;
import java.util.List;

public class InventoryService {
    private ProductRepository productRepository;

    public InventoryService() {
        this.productRepository = new ProductRepository();
    }

    public List<ProductData> getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        List<ProductData> productDataList = new ArrayList<>();
        
        for (Product product : products) {
            ProductData productData = new ProductData(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getStock(),
                product.getPrice().toString(),
                product.getImageUrl()
            );
            productDataList.add(productData);
        }
        
        return productDataList;
    }

    public List<ProductData> getProductsByCategory(String category) {
        try {
            System.out.println("InventoryService: Getting products for category: " + category);
            List<Product> products = productRepository.getProductsByCategory(category);
            System.out.println("InventoryService: Found " + products.size() + " products from repository");
            
            List<ProductData> productDataList = new ArrayList<>();
            
            for (Product product : products) {
                ProductData productData = new ProductData(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategory(),
                    product.getStock(),
                    product.getPrice().toString(),
                    product.getImageUrl()
                );
                productDataList.add(productData);
            }
            
            System.out.println("InventoryService: Converted to " + productDataList.size() + " ProductData objects");
            return productDataList;
        } catch (Exception e) {
            System.err.println("ERROR in InventoryService.getProductsByCategory: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public ProductData getProductById(Long id) {
        return productRepository.getProductById(id)
            .map(product -> new ProductData(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getStock(),
                product.getPrice().toString(),
                product.getImageUrl()
            ))
            .orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.createProduct(product);
    }

    public boolean updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }

    public boolean updateStock(Long productId, int newStock) {
        return productRepository.updateStock(productId, newStock);
    }

    public boolean deleteProduct(Long id) {
        return productRepository.deleteProduct(id);
    }

    public void initializeProducts() {
        productRepository.initializeProducts();
    }
}