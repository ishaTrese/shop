package com.project.shop;

import com.project.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody Signup signupRequest) {
        if (signupRequest.firstName == null || signupRequest.firstName.trim().isEmpty() ||
                signupRequest.lastName == null || signupRequest.lastName.trim().isEmpty() ||
                signupRequest.email == null || signupRequest.email.trim().isEmpty() ||
                signupRequest.mobileNumber == null || signupRequest.mobileNumber.trim().isEmpty() ||
                signupRequest.buildingStreet == null || signupRequest.buildingStreet.trim().isEmpty() ||
                signupRequest.barangayDistrict == null || signupRequest.barangayDistrict.trim().isEmpty() ||
                signupRequest.cityMunicipality == null || signupRequest.cityMunicipality.trim().isEmpty() ||
                signupRequest.postalCode == null || signupRequest.postalCode.trim().isEmpty() ||
                signupRequest.password == null || signupRequest.password.trim().isEmpty()) {

            Response errorResponse = Response.message(
                    "All fields are required. Please provide all information.",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            String sql = "INSERT INTO users (" +
                    "first_name, last_name, email, mobile_number, " +
                    "building_street, barangay_district, city_municipality, postal_code, password" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = AppConfig.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, signupRequest.firstName);
            stmt.setString(2, signupRequest.lastName);
            stmt.setString(3, signupRequest.email);
            stmt.setString(4, signupRequest.mobileNumber);
            stmt.setString(5, signupRequest.buildingStreet);
            stmt.setString(6, signupRequest.barangayDistrict);
            stmt.setString(7, signupRequest.cityMunicipality);
            stmt.setString(8, signupRequest.postalCode);
            stmt.setString(9, signupRequest.password);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                Response successResponse = Response.message(
                        "User registered successfully! Welcome " + signupRequest.email,
                        RESPONSE_TYPE.SUCCESS
                );
                return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
            } else {
                Response errorResponse = Response.message(
                        "User registration failed. Try again.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Database error: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Login loginRequest) {
        if (loginRequest.email == null || loginRequest.email.trim().isEmpty() ||
                loginRequest.password == null || loginRequest.password.trim().isEmpty()) {

            Response errorResponse = Response.message(
                    "Email and password are required.",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            String sql = """
                    SELECT id, first_name, last_name, building_street, barangay_district, city_municipality, postal_code, email, mobile_number\s
                    FROM users\s
                    WHERE email = ? AND password = ?
                    """;
            PreparedStatement stmt = AppConfig.connection.prepareStatement(sql);
            stmt.setString(1, loginRequest.email);
            stmt.setString(2, loginRequest.password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                AppConfig.user = rs.getInt("id");
                AppConfig.userName = rs.getString("first_name") + " " + rs.getString("last_name");

                AppConfig.userAddress = rs.getString("building_street") + ", " +
                        rs.getString("barangay_district") + ", " +
                        rs.getString("city_municipality") + ", " +
                        rs.getString("postal_code");

                AppConfig.userEmail = rs.getString("email");
                AppConfig.userPhone = rs.getString("mobile_number");

                Response successResponse = Response.message(
                        "Login successful! Welcome back " + loginRequest.email,
                        RESPONSE_TYPE.SUCCESS
                );
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                Response errorResponse = Response.message(
                        "Invalid email or password.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Database error: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestBody Logout logoutRequest) {
        if (logoutRequest.message != null || !logoutRequest.message.equalsIgnoreCase("logout")) {
            AppConfig.user = 0;
            Response successResponse = Response.message(
                    "Logout successful!",
                    RESPONSE_TYPE.SUCCESS
            );
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } else {
            Response errorResponse = Response.message(
                    "Logout unsuccessful!",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<Response> addToCart(@RequestBody CartItem addRequest) {
        int userId = AppConfig.getCurrentUser();
        if (userId == 0) {
            Response errorResponse = Response.message(
                    "User not logged in",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        // Get product from database using product ID
        InventoryService inventoryService = new InventoryService();
        ProductData productData = inventoryService.getProductById(addRequest.getProductId());
        
        if (productData == null) {
            return new ResponseEntity<>(
                    Response.message("Product not found", RESPONSE_TYPE.ERROR),
                    HttpStatus.NOT_FOUND
            );
        }

        // Validate requested quantity
        if (addRequest.getQuantity() <= 0) {
            return new ResponseEntity<>(
                    Response.message("Quantity to add must be at least 1.", RESPONSE_TYPE.ERROR),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Check if enough stock is available
        if (productData.getStock() < addRequest.getQuantity()) {
            return new ResponseEntity<>(
                    Response.message("Not enough stock for " + productData.getName() + ". Available: " + productData.getStock() + ".", RESPONSE_TYPE.ERROR),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            String checkSql = "SELECT quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
            PreparedStatement checkStmt = AppConfig.connection.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setLong(2, addRequest.getProductId());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // If item already in cart, update its quantity
                int existingQuantityInCart = rs.getInt("quantity");
                int newTotalQuantityInCart = existingQuantityInCart + addRequest.getQuantity();

                String updateSql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
                PreparedStatement updateStmt = AppConfig.connection.prepareStatement(updateSql);
                updateStmt.setInt(1, newTotalQuantityInCart);
                updateStmt.setInt(2, userId);
                updateStmt.setLong(3, addRequest.getProductId());
                updateStmt.executeUpdate();
            } else {
                // If item not in cart, insert it
                String insertSql = "INSERT INTO cart_items " +
                        "(user_id, product_id, product_name, category, price, quantity) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = AppConfig.connection.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setLong(2, addRequest.getProductId());
                insertStmt.setString(3, productData.getName());
                insertStmt.setString(4, productData.getCategory());
                insertStmt.setBigDecimal(5, new java.math.BigDecimal(productData.getPrice()));
                insertStmt.setInt(6, addRequest.getQuantity());
                insertStmt.executeUpdate();
            }

            // Update stock in database
            int newStock = productData.getStock() - addRequest.getQuantity();
            inventoryService.updateStock(addRequest.getProductId(), newStock);

            return new ResponseEntity<>(
                    Response.message("Added " + addRequest.getQuantity() + " of " + productData.getName() + " to cart successfully! Available stock: " + newStock + ".", RESPONSE_TYPE.SUCCESS),
                    HttpStatus.OK
            );

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    Response.message("Database error: " + e.getMessage(), RESPONSE_TYPE.ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/remove-from-cart")
    public ResponseEntity<Response> removeFromCart(@RequestBody RemoveFromCart request) {
        int userId = AppConfig.getCurrentUser();
        if (userId == 0) {
            Response errorResponse = Response.message(
                    "User not logged in",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        try {
            String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
            PreparedStatement stmt = AppConfig.connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setLong(2, request.productId);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                Response successResponse = Response.message(
                        "Item removed from cart successfully.",
                        RESPONSE_TYPE.SUCCESS
                );
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                Response errorResponse = Response.message(
                        "Item not found in cart.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Database error: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/place-order")
    public ResponseEntity<Response> placeOrder(@RequestBody PlaceOrder orderRequest) {
        int userId = AppConfig.getCurrentUser();
        if (userId == 0) {
            Response errorResponse = Response.message(
                    "User not logged in. Please log in to place an order.",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        if (orderRequest.shippingDetails == null ||
                orderRequest.shippingDetails.fullName == null || orderRequest.shippingDetails.fullName.trim().isEmpty() ||
                orderRequest.shippingDetails.address == null || orderRequest.shippingDetails.address.trim().isEmpty() ||
                orderRequest.shippingDetails.email == null || orderRequest.shippingDetails.email.trim().isEmpty() ||
                orderRequest.shippingDetails.mobileNumber == null || orderRequest.shippingDetails.mobileNumber.trim().isEmpty()) {
            Response errorResponse = Response.message(
                    "Missing or incomplete shipping details.",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {

            AppConfig.connection.setAutoCommit(false);


            List<Map<String, Object>> cartItems = new ArrayList<>();
            float subtotal = 0;

            String getCartSql = "SELECT product_name, price, quantity, product_id, category FROM cart_items WHERE user_id = ?";
            PreparedStatement getCartStmt = AppConfig.connection.prepareStatement(getCartSql);
            getCartStmt.setInt(1, userId);
            ResultSet rs = getCartStmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("product_name");
                float price = rs.getFloat("price");
                int quantity = rs.getInt("quantity");
                Long productId = rs.getLong("product_id");
                String category = rs.getString("category");

                Map<String, Object> item = new HashMap<>();
                item.put("name", name);
                item.put("price", price);
                item.put("quantity", quantity);
                item.put("productId", productId);
                item.put("category", category);
                cartItems.add(item);
                subtotal += (price * quantity);
            }

            if (cartItems.isEmpty()) {
                AppConfig.connection.rollback();
                Response errorResponse = Response.message(
                        "Your cart is empty. Please add items before placing an order.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Calculate tax (3% tax rate)
            int taxRate = 3;
            float tax = subtotal * taxRate / 100f;
            float orderTotal = subtotal + tax;


            String insertOrderSql = "INSERT INTO orders (user_id, order_date, total_amount, shipping_full_name, shipping_address, shipping_email, shipping_mobile_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertOrderStmt = AppConfig.connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);

            insertOrderStmt.setInt(1, userId);
            insertOrderStmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            insertOrderStmt.setFloat(3, orderTotal);
            insertOrderStmt.setString(4, orderRequest.shippingDetails.fullName);
            insertOrderStmt.setString(5, orderRequest.shippingDetails.address);
            insertOrderStmt.setString(6, orderRequest.shippingDetails.email);
            insertOrderStmt.setString(7, orderRequest.shippingDetails.mobileNumber);

            int rowsInserted = insertOrderStmt.executeUpdate();
            if (rowsInserted == 0) {
                AppConfig.connection.rollback();
                Response errorResponse = Response.message(
                        "Failed to create order record.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ResultSet generatedKeys = insertOrderStmt.getGeneratedKeys();
            int orderId;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            } else {
                AppConfig.connection.rollback();
                Response errorResponse = Response.message(
                        "Failed to retrieve order ID.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String insertOrderItemSql = "INSERT INTO order_items (order_id, product_name, category, product_id, price, quantity) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertOrderItemStmt = AppConfig.connection.prepareStatement(insertOrderItemSql);

            for (Map<String, Object> item : cartItems) {
                insertOrderItemStmt.setInt(1, orderId);
                insertOrderItemStmt.setString(2, (String) item.get("name"));
                insertOrderItemStmt.setString(3, (String) item.get("category"));
                insertOrderItemStmt.setLong(4, (Long) item.get("productId"));
                insertOrderItemStmt.setFloat(5, (Float) item.get("price"));
                insertOrderItemStmt.setInt(6, (Integer) item.get("quantity"));
                insertOrderItemStmt.addBatch();
            }
            insertOrderItemStmt.executeBatch();

            String clearCartSql = "DELETE FROM cart_items WHERE user_id = ?";
            PreparedStatement clearCartStmt = AppConfig.connection.prepareStatement(clearCartSql);
            clearCartStmt.setInt(1, userId);
            clearCartStmt.executeUpdate();

            AppConfig.connection.commit();
            AppConfig.connection.setAutoCommit(true);

            Response successResponse = Response.message(
                    "Order placed successfully! Order ID: " + orderId,
                    RESPONSE_TYPE.SUCCESS
            );
            return new ResponseEntity<>(successResponse, HttpStatus.OK);

        } catch (SQLException e) {
            try {
                AppConfig.connection.rollback();
                AppConfig.connection.setAutoCommit(true);
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Database error during order placement: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/inventory/update-stock")
    public ResponseEntity<Response> updateStock(@RequestBody Map<String, Object> requestBody) {
        String category = (String) requestBody.get("category");
        String productName = (String) requestBody.get("productName");
        Integer newStock = (Integer) requestBody.get("new_stock");

        if (category == null || category.trim().isEmpty() ||
                productName == null || productName.trim().isEmpty() ||
                newStock == null || newStock < 0) {
            Response errorResponse = Response.message(
                    "Invalid request data. Please provide 'category', 'productName', and a non-negative 'new_stock'.",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            // Find the product in the database
            String sql = "SELECT id, name FROM products WHERE category = ? AND name = ?";
            PreparedStatement stmt = AppConfig.connection.prepareStatement(sql);
            stmt.setString(1, category);
            stmt.setString(2, productName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Long productId = rs.getLong("id");
                String foundProductName = rs.getString("name");
                
                // Update the stock
                InventoryService inventoryService = new InventoryService();
                boolean updated = inventoryService.updateStock(productId, newStock);
                
                if (updated) {
                    Response successResponse = Response.message(
                            "Stock for '" + foundProductName + "' updated successfully to " + newStock + ".",
                            RESPONSE_TYPE.SUCCESS
                    );
                    return new ResponseEntity<>(successResponse, HttpStatus.OK);
                } else {
                    Response errorResponse = Response.message(
                            "Failed to update stock for '" + foundProductName + "'.",
                            RESPONSE_TYPE.ERROR
                    );
                    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                Response errorResponse = Response.message(
                        "Product '" + productName + "' not found in category '" + category + "'.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Database error: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Response> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty() ||
                product.getDescription() == null || product.getDescription().trim().isEmpty() ||
                product.getCategory() == null || product.getCategory().trim().isEmpty() ||
                product.getPrice() == null || product.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0 ||
                product.getStock() < 0) {
            
            Response errorResponse = Response.message(
                    "Invalid product data. Please provide all required fields with valid values.",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            InventoryService inventoryService = new InventoryService();
            Product createdProduct = inventoryService.createProduct(product);
            
            if (createdProduct != null) {
                Response successResponse = Response.message(
                        "Product '" + product.getName() + "' created successfully!",
                        RESPONSE_TYPE.SUCCESS
                );
                return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
            } else {
                Response errorResponse = Response.message(
                        "Failed to create product.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Error creating product: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty() ||
                product.getDescription() == null || product.getDescription().trim().isEmpty() ||
                product.getCategory() == null || product.getCategory().trim().isEmpty() ||
                product.getPrice() == null || product.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0 ||
                product.getStock() < 0) {
            
            Response errorResponse = Response.message(
                    "Invalid product data. Please provide all required fields with valid values.",
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            product.setId(id);
            InventoryService inventoryService = new InventoryService();
            boolean updated = inventoryService.updateProduct(product);
            
            if (updated) {
                Response successResponse = Response.message(
                        "Product '" + product.getName() + "' updated successfully!",
                        RESPONSE_TYPE.SUCCESS
                );
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                Response errorResponse = Response.message(
                        "Product not found or failed to update.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Error updating product: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
        try {
            InventoryService inventoryService = new InventoryService();
            boolean deleted = inventoryService.deleteProduct(id);
            
            if (deleted) {
                Response successResponse = Response.message(
                        "Product deleted successfully!",
                        RESPONSE_TYPE.SUCCESS
                );
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                Response errorResponse = Response.message(
                        "Product not found or failed to delete.",
                        RESPONSE_TYPE.ERROR
                );
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Response errorResponse = Response.message(
                    "Error deleting product: " + e.getMessage(),
                    RESPONSE_TYPE.ERROR
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductData>> getAllProducts() {
        try {
            InventoryService inventoryService = new InventoryService();
            List<ProductData> products = inventoryService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductData> getProductById(@PathVariable Long id) {
        try {
            InventoryService inventoryService = new InventoryService();
            ProductData product = inventoryService.getProductById(id);
            
            if (product != null) {
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}