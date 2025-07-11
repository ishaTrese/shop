package com.project.shop;

import com.project.model.ProductData;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class RootController {
    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        model.addAttribute("contentType", "home");
        return "index";
    }

    @GetMapping("/categories/{name}")
    public String catalog(@PathVariable String name, Model model, 
                         @RequestParam(value = "sort", required = false) String sort) {
        try {
            if (name.equalsIgnoreCase("rings")     ||
                name.equalsIgnoreCase("earrings")  ||
                name.equalsIgnoreCase("necklaces") ||
                name.equalsIgnoreCase("bracelets"))
            {
                String type;
                switch (name) {
                    case "bracelets":
                        type = "A";
                        break;
                    case "earrings":
                        type = "B";
                        break;
                    case "necklaces":
                        type = "C";
                        break;
                    case "rings":
                        type = "D";
                        break;
                    default:
                        type = "none";
                        break;
                }
                
                // Get products from database for this category
                System.out.println("Fetching products for category: " + name);
                InventoryService inventoryService = new InventoryService();
                List<ProductData> products = inventoryService.getProductsByCategory(StringUtils.capitalize(name));
                System.out.println("Found " + products.size() + " products for category: " + name);
                
                // Sort products if sort parameter is provided
                if (sort != null && !sort.isEmpty()) {
                    if ("low".equals(sort)) {
                        // Sort by price: low to high
                        products.sort((p1, p2) -> {
                            try {
                                float price1 = Float.parseFloat(p1.getPrice());
                                float price2 = Float.parseFloat(p2.getPrice());
                                return Float.compare(price1, price2);
                            } catch (NumberFormatException e) {
                                return 0; // Keep original order if parsing fails
                            }
                        });
                    } else if ("high".equals(sort)) {
                        // Sort by price: high to low
                        products.sort((p1, p2) -> {
                            try {
                                float price1 = Float.parseFloat(p1.getPrice());
                                float price2 = Float.parseFloat(p2.getPrice());
                                return Float.compare(price2, price1);
                            } catch (NumberFormatException e) {
                                return 0; // Keep original order if parsing fails
                            }
                        });
                    }
                }
                
                model.addAttribute("type", type);
                model.addAttribute("title", StringUtils.capitalize(name));
                model.addAttribute("contentType", "catalog");
                model.addAttribute("node", name.toLowerCase());
                model.addAttribute("nodeName", name.toUpperCase());
                model.addAttribute("category", name.toLowerCase());
                model.addAttribute("products", products);
                model.addAttribute("currentSort", sort != null ? sort : "low");
                return "index";
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("ERROR in catalog method for category '" + name + "': " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading catalog: " + e.getMessage());
        }
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        return "redirect:/home";
    }
    
    @GetMapping("/account")
    public String account(Model model) {
        if (AppConfig.getCurrentUser() == 0) {
            return "redirect:/account/login";
        }
        model.addAttribute("title", "Account");
        model.addAttribute("contentType", "account");

        model.addAttribute("name", AppConfig.userName);
        model.addAttribute("address", AppConfig.userAddress);
        model.addAttribute("email", AppConfig.userEmail);
        model.addAttribute("phone", AppConfig.userPhone);
        if (AppConfig.userEmail.equalsIgnoreCase("admin@shop.com")) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isAdmin", false);
        }
        return "index";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        if (AppConfig.getCurrentUser() == 0) {
            return "redirect:/account/login";
        }

        model.addAttribute("title", "Cart");
        model.addAttribute("contentType", "cart");

        List<Map<String, Object>> cartItems = new ArrayList<>();
        float subtotal = 0;
        float shippingFeePerItem = 10f; // TODO: change this to 0 for simplicity

        try {
            String sql = "SELECT product_name, price, quantity, product_id, category FROM cart_items WHERE user_id = ?";
            PreparedStatement stmt = AppConfig.connection.prepareStatement(sql);
            stmt.setInt(1, AppConfig.getCurrentUser());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("product_name");
                float price = rs.getFloat("price");
                int quantity = rs.getInt("quantity");
                float total = price * quantity;
                float shippingFee = 0;
                float totalWithShipping = total + shippingFee;

                Long productId = rs.getLong("product_id");
                String category = rs.getString("category");

                Map<String, Object> item = new HashMap<>();
                item.put("name", name);
                item.put("price", price);
                item.put("quantity", quantity);
                item.put("shippingFee", shippingFee);
                item.put("total", totalWithShipping);
                item.put("productId", productId);
                item.put("category", category);
                cartItems.add(item);
                subtotal += total;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int taxRate = 3;
        float tax = subtotal * taxRate / 100f;
        float total = subtotal + tax;

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("flag", cartItems.size());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", taxRate);
        model.addAttribute("taxAmount", tax);
        model.addAttribute("total", total);

        return "index";
    }


    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (AppConfig.getCurrentUser() == 0) {
            return "redirect:/account/login";
        }
        model.addAttribute("title", "Checkout");
        model.addAttribute("contentType", "checkout");

        model.addAttribute("name", AppConfig.userName);
        model.addAttribute("address", AppConfig.userAddress);
        model.addAttribute("email",   AppConfig.userEmail);
        model.addAttribute("number",  AppConfig.userPhone);
        return "index";
    }

    @GetMapping("/account/login")
    public String login(Model model) {
        if (AppConfig.getCurrentUser() != 0) {
            return "redirect:/account";
        }
        model.addAttribute("title", "Login");
        model.addAttribute("contentType", "login");
        return "index";
    }

    @GetMapping("/account/signup")
    public String signup(Model model) {
        if (AppConfig.getCurrentUser() != 0) {
            return "redirect:/account";
        }
        model.addAttribute("title", "Signup");
        model.addAttribute("contentType", "signup");
        return "index";
    }

    @GetMapping("/product/{id}")
    public String productController(@PathVariable Long id, Model model) {
        model.addAttribute("contentType", "product");
        
        // Get product from database using the product ID
        InventoryService inventoryService = new InventoryService();
        ProductData product = inventoryService.getProductById(id);
        
        if (product != null) {
            String category = product.getCategory();
            String categoryLower = category.toLowerCase();
            
            model.addAttribute("title", product.getName());
            model.addAttribute("name", product.getName());
            model.addAttribute("description", product.getDescription());
            model.addAttribute("price", product.getPrice());
            model.addAttribute("category", categoryLower);
            model.addAttribute("productId", product.getId());
            model.addAttribute("imageUrl", product.getImageUrl());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        
        return "index";
    }


    @GetMapping("/account/orders")
    public String orders(Model model) {
        int userId = AppConfig.getCurrentUser();
        if (userId == 0) {
            return "redirect:/account/login";
        }

        model.addAttribute("title", "Your Orders");
        model.addAttribute("contentType", "order");

        List<Map<String, Object>> userOrders = new ArrayList<>();

        try {
            String getOrdersSql = "SELECT id, order_date, total_amount, shipping_full_name, shipping_address, shipping_email, shipping_mobile_number, status FROM orders WHERE user_id = ? ORDER BY order_date DESC";
            PreparedStatement getOrdersStmt = AppConfig.connection.prepareStatement(getOrdersSql);
            getOrdersStmt.setInt(1, userId);
            ResultSet ordersRs = getOrdersStmt.executeQuery();

            while (ordersRs.next()) {
                int orderId = ordersRs.getInt("id");
                String orderDate = ordersRs.getTimestamp("order_date").toLocalDateTime().toLocalDate().toString(); // Format date
                float totalAmount = ordersRs.getFloat("total_amount");
                String shippingFullName = ordersRs.getString("shipping_full_name");
                String shippingAddress = ordersRs.getString("shipping_address");
                String shippingEmail = ordersRs.getString("shipping_email");
                String shippingMobileNumber = ordersRs.getString("shipping_mobile_number");
                String status = ordersRs.getString("status");

                Map<String, Object> order = new HashMap<>();
                order.put("orderId", orderId);
                order.put("orderDate", orderDate);
                order.put("totalAmount", String.format("₱ %.2f", totalAmount));
                order.put("shippingFullName", shippingFullName);
                order.put("shippingAddress", shippingAddress);
                order.put("shippingEmail", shippingEmail);
                order.put("shippingMobileNumber", shippingMobileNumber);
                order.put("status", status);

                List<Map<String, Object>> orderItems = new ArrayList<>();
                String getOrderItemsSql = "SELECT product_name, category, price, quantity FROM order_items WHERE order_id = ?";
                PreparedStatement getOrderItemsStmt = AppConfig.connection.prepareStatement(getOrderItemsSql);
                getOrderItemsStmt.setInt(1, orderId);
                ResultSet itemsRs = getOrderItemsStmt.executeQuery();

                while (itemsRs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("productName", itemsRs.getString("product_name"));
                    item.put("category", itemsRs.getString("category"));
                    item.put("price", String.format("₱ %.2f", itemsRs.getFloat("price")));
                    item.put("quantity", itemsRs.getInt("quantity"));
                    orderItems.add(item);
                }
                order.put("items", orderItems);
                userOrders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to retrieve orders due to a database error.");
        }

        model.addAttribute("orders", userOrders);

        return "index";
    }

    @GetMapping("/admin-panel")
    public String admin(Model model) {
        int userId = AppConfig.getCurrentUser();
        if (userId == 0) {
            return "redirect:/account/login";
        }
        model.addAttribute("title", "Admin Panel");
        model.addAttribute("contentType", "admin-panel");

        if (!AppConfig.userEmail.equalsIgnoreCase("admin@shop.com")) {
            return "redirect:/home";
        }
        
        List<Map<String, Object>> orders = new ArrayList<>();
        List<Map<String, Object>> recentOrders = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalUsers = 0;
        BigDecimal averageOrderValue = BigDecimal.ZERO;
        String topCategory = "N/A";
        int activeOrderCount = 0;
        
        try {
            // Get all orders (including cancelled for display purposes)
            String getOrdersSql = "SELECT id, order_date, total_amount, shipping_full_name, shipping_address, shipping_email, shipping_mobile_number, status FROM orders ORDER BY order_date DESC";
            PreparedStatement getOrdersStmt = AppConfig.connection.prepareStatement(getOrdersSql);
            ResultSet ordersRs = getOrdersStmt.executeQuery();

            while (ordersRs.next()) {
                int orderId = ordersRs.getInt("id");
                LocalDateTime orderDate = ordersRs.getTimestamp("order_date").toLocalDateTime();
                BigDecimal totalAmount = BigDecimal.valueOf(ordersRs.getFloat("total_amount"));
                String shippingFullName = ordersRs.getString("shipping_full_name");
                String shippingAddress = ordersRs.getString("shipping_address");
                String shippingEmail = ordersRs.getString("shipping_email");
                String shippingMobileNumber = ordersRs.getString("shipping_mobile_number");
                String status = ordersRs.getString("status");

                Map<String, Object> order = new HashMap<>();
                order.put("id", orderId);
                order.put("orderDate", orderDate);
                order.put("totalAmount", totalAmount);
                order.put("shippingFullName", shippingFullName);
                order.put("shippingAddress", shippingAddress);
                order.put("shippingEmail", shippingEmail);
                order.put("shippingMobileNumber", shippingMobileNumber);
                order.put("status", status);

                // Add to recent orders (last 5) - exclude cancelled orders
                if (recentOrders.size() < 5 && !"Cancelled".equals(status)) {
                    recentOrders.add(order);
                }

                // Only include non-cancelled orders in revenue calculation
                if (!"Cancelled".equals(status)) {
                    totalRevenue = totalRevenue.add(totalAmount);
                }

                List<Map<String, Object>> orderItems = new ArrayList<>();
                String getOrderItemsSql = "SELECT product_name, category, price, quantity FROM order_items WHERE order_id = ?";
                PreparedStatement getOrderItemsStmt = AppConfig.connection.prepareStatement(getOrderItemsSql);
                getOrderItemsStmt.setInt(1, orderId);
                ResultSet itemsRs = getOrderItemsStmt.executeQuery();

                while (itemsRs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("productName", itemsRs.getString("product_name"));
                    item.put("category", itemsRs.getString("category"));
                    item.put("price", BigDecimal.valueOf(itemsRs.getFloat("price")));
                    item.put("quantity", itemsRs.getInt("quantity"));
                    orderItems.add(item);
                }
                order.put("items", orderItems);
                orders.add(order);

                itemsRs.close();
                getOrderItemsStmt.close();
            }

            ordersRs.close();
            getOrdersStmt.close();

            // Calculate average order value (excluding cancelled orders)
            for (Map<String, Object> order : orders) {
                if (!"Cancelled".equals(order.get("status"))) {
                    activeOrderCount++;
                }
            }
            
            if (activeOrderCount > 0) {
                averageOrderValue = totalRevenue.divide(BigDecimal.valueOf(activeOrderCount), 2, RoundingMode.HALF_UP);
            }

            // Get total users (excluding users who only have cancelled orders)
            String countUsersSql = "SELECT COUNT(DISTINCT user_id) as user_count FROM orders WHERE status != 'Cancelled'";
            PreparedStatement countUsersStmt = AppConfig.connection.prepareStatement(countUsersSql);
            ResultSet usersRs = countUsersStmt.executeQuery();
            if (usersRs.next()) {
                totalUsers = usersRs.getInt("user_count");
            }
            usersRs.close();
            countUsersStmt.close();

            // Get top category (excluding cancelled orders)
            String topCategorySql = """
                SELECT oi.category, SUM(oi.price * oi.quantity) as total_sales 
                FROM order_items oi 
                JOIN orders o ON oi.order_id = o.id 
                WHERE o.status != 'Cancelled' 
                GROUP BY oi.category 
                ORDER BY total_sales DESC 
                LIMIT 1
                """;
            PreparedStatement topCategoryStmt = AppConfig.connection.prepareStatement(topCategorySql);
            ResultSet categoryRs = topCategoryStmt.executeQuery();
            if (categoryRs.next()) {
                topCategory = categoryRs.getString("category");
            }
            categoryRs.close();
            topCategoryStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to retrieve orders due to a database error.");
        }

        // Get products for inventory
        InventoryService inventoryService = new InventoryService();
        List<ProductData> products = inventoryService.getAllProducts();

        model.addAttribute("orders", orders);
        model.addAttribute("products", products);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("totalRevenue", String.format("%.2f", totalRevenue));
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("averageOrderValue", String.format("%.2f", averageOrderValue));
        model.addAttribute("topCategory", topCategory);
        model.addAttribute("activeOrderCount", activeOrderCount);
        
        return "index";
    }

}
