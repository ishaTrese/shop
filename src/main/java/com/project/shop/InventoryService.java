package com.project.shop;

import com.project.model.ProductData;

import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    public List<ProductData> getAllProducts() {
        List<ProductData> products = new ArrayList<>();
        long idCounter = 1L;

        idCounter = processCategory(products, AppConfig.bracelets, "Bracelets", idCounter);
        idCounter = processCategory(products, AppConfig.earrings, "Earrings", idCounter);
        idCounter = processCategory(products, AppConfig.necklaces, "Necklaces", idCounter);
        idCounter = processCategory(products, AppConfig.rings, "Rings", idCounter);

        return products;
    }

    private long processCategory(List<ProductData> allProducts, String[][] categoryData, String categoryName, long idCounter) {
        for (String[] item : categoryData) {
            if (item.length >= 4) {
                String name = item[0];
                String description = item[1];
                String price = item[2];
                int stock = Integer.parseInt(item[3]);
                allProducts.add(new ProductData(idCounter++, name, description, categoryName, stock, price));
            }
        }
        return idCounter;
    }
}