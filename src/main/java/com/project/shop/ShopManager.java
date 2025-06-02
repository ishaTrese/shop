package com.project.shop;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/shop";
    private static final String USER = "root";
    private static final String PASSWORD = "your_mysql_root_password";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public int addUser(String firstName, String lastName, String email, String mobileNumber,
                       String buildingStreet, String barangayDistrict, String cityMunicipality,
                       String postalCode) {
        String SQL = "INSERT INTO users (first_name, last_name, email, mobile_number, building_street, barangay_district, city_municipality, postal_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, mobileNumber);
            pstmt.setString(5, buildingStreet);
            pstmt.setString(6, barangayDistrict);
            pstmt.setString(7, cityMunicipality);
            pstmt.setString(8, postalCode);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding user: " + ex.getMessage());
        }
        return generatedId;
    }

    public void getUserById(int userId) {
        String SQL = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("--- User Details ---");
                    System.out.println("ID: " + rs.getInt("user_id"));
                    System.out.println("Name: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Mobile: " + rs.getString("mobile_number"));
                    System.out.println("Address: " + rs.getString("building_street") + ", " +
                            rs.getString("barangay_district") + ", " +
                            rs.getString("city_municipality") + ", " +
                            rs.getString("postal_code"));
                    System.out.println("Created At: " + rs.getTimestamp("created_at").toLocalDateTime());
                    System.out.println("Updated At: " + rs.getTimestamp("updated_at").toLocalDateTime());
                    System.out.println("--------------------");
                } else {
                    System.out.println("User with ID " + userId + " not found.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting user by ID: " + ex.getMessage());
        }
    }

    public boolean updateUserMobile(int userId, String newMobileNumber) {
        String SQL = "UPDATE users SET mobile_number = ? WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, newMobileNumber);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("Error updating user mobile: " + ex.getMessage());
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String SQL = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("Error deleting user: " + ex.getMessage());
            return false;
        }
    }

    public int addProduct(String productName, String productType, BigDecimal price, int stockAvailable) {
        String SQL = "INSERT INTO products (product_name, product_type, price, stock_available) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, productName);
            pstmt.setString(2, productType);
            pstmt.setBigDecimal(3, price);
            pstmt.setInt(4, stockAvailable);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding product: " + ex.getMessage());
        }
        return generatedId;
    }

    public void getProductById(int productId) {
        String SQL = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("--- Product Details ---");
                    System.out.println("ID: " + rs.getInt("product_id"));
                    System.out.println("Name: " + rs.getString("product_name"));
                    System.out.println("Type: " + rs.getString("product_type"));
                    System.out.println("Price: " + rs.getBigDecimal("price"));
                    System.out.println("Stock: " + rs.getInt("stock_available"));
                    System.out.println("Created At: " + rs.getTimestamp("created_at").toLocalDateTime());
                    System.out.println("Updated At: " + rs.getTimestamp("updated_at").toLocalDateTime());
                    System.out.println("--------------------");
                } else {
                    System.out.println("Product with ID " + productId + " not found.");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error getting product by ID: " + ex.getMessage());
        }
    }

    public boolean updateProductStock(int productId, int newStock) {
        String SQL = "UPDATE products SET stock_available = ? WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, newStock);
            pstmt.setInt(2, productId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("Error updating product stock: " + ex.getMessage());
            return false;
        }
    }

    public int addOrder(int userId, BigDecimal totalAmount, String orderStatus,
                        String shippingAddressBuildingStreet, String shippingAddressBarangayDistrict,
                        String shippingAddressCityMunicipality, String shippingAddressPostalCode) {
        String SQL = "INSERT INTO orders (user_id, total_amount, order_status, shipping_address_building_street, " +
                "shipping_address_barangay_district, shipping_address_city_municipality, shipping_address_postal_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, userId);
            pstmt.setBigDecimal(2, totalAmount);
            pstmt.setString(3, orderStatus);
            pstmt.setString(4, shippingAddressBuildingStreet);
            pstmt.setString(5, shippingAddressBarangayDistrict);
            pstmt.setString(6, shippingAddressCityMunicipality);
            pstmt.setString(7, shippingAddressPostalCode);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding order: " + ex.getMessage());
        }
        return generatedId;
    }

    public void getOrderDetails(int orderId) {
        String orderSQL = "SELECT * FROM orders WHERE order_id = ?";
        String itemsSQL = "SELECT oi.*, p.product_name, p.product_type FROM order_items oi JOIN products p ON oi.product_id = p.product_id WHERE oi.order_id = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement pstmt = conn.prepareStatement(orderSQL)) {
                pstmt.setInt(1, orderId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("--- Order Details (ID: " + rs.getInt("order_id") + ") ---");
                        System.out.println("User ID: " + rs.getInt("user_id"));
                        System.out.println("Order Date: " + rs.getTimestamp("order_date").toLocalDateTime());
                        System.out.println("Total Amount: " + rs.getBigDecimal("total_amount"));
                        System.out.println("Status: " + rs.getString("order_status"));
                        System.out.println("Shipping Address: " + rs.getString("shipping_address_building_street") + ", " +
                                rs.getString("shipping_address_barangay_district") + ", " +
                                rs.getString("shipping_address_city_municipality") + ", " +
                                rs.getString("shipping_address_postal_code"));
                        System.out.println("--------------------");
                    } else {
                        System.out.println("Order with ID " + orderId + " not found.");
                        return;
                    }
                }
            }

            System.out.println("--- Order Items ---");
            try (PreparedStatement pstmt = conn.prepareStatement(itemsSQL)) {
                pstmt.setInt(1, orderId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    boolean hasItems = false;
                    while (rs.next()) {
                        hasItems = true;
                        System.out.println("  - Item ID: " + rs.getInt("order_item_id") +
                                ", Product: " + rs.getString("product_name") +
                                " (" + rs.getString("product_type") + ")" +
                                ", Qty: " + rs.getInt("quantity") +
                                ", Price at Order: " + rs.getBigDecimal("price_at_order"));
                    }
                    if (!hasItems) {
                        System.out.println("  No items found for this order.");
                    }
                }
            }
            System.out.println("--------------------");

        } catch (SQLException ex) {
            System.err.println("Error getting order details: " + ex.getMessage());
        }
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String SQL = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("Error updating order status: " + ex.getMessage());
            return false;
        }
    }

    public int addOrderItem(int orderId, int productId, int quantity, BigDecimal priceAtOrder) {
        String SQL = "INSERT INTO order_items (order_id, product_id, quantity, price_at_order) VALUES (?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setBigDecimal(4, priceAtOrder);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding order item: " + ex.getMessage());
        }
        return generatedId;
    }

    public boolean deleteOrderItemsByOrderId(int orderId) {
        String SQL = "DELETE FROM order_items WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("Error deleting order items by order ID: " + ex.getMessage());
            return false;
        }
    }

    public boolean deleteOrder(int orderId) {
        boolean itemsDeleted = deleteOrderItemsByOrderId(orderId);
        if (!itemsDeleted) {
            System.err.println("Could not delete order items for order " + orderId + ", skipping order deletion.");
            return false;
        }

        String SQL = "DELETE FROM orders WHERE order_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, orderId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            System.err.println("Error deleting order: " + ex.getMessage());
            return false;
        }
    }
}