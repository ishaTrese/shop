package com.project.shop;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public String catalog(@PathVariable String name, Model model) {
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
            model.addAttribute("type", type);
            model.addAttribute("title", StringUtils.capitalize(name));
            model.addAttribute("contentType", "catalog");
            model.addAttribute("node", name.toLowerCase());
            model.addAttribute("nodeName", name.toUpperCase());
            return "index";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
        float shippingFeePerItem = 10f;

        try {
            String sql = "SELECT product_name, price, quantity, product_index, category FROM cart_items WHERE user_id = ?";
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

                int productIndex = rs.getInt("product_index");
                String category = rs.getString("category");

                Map<String, Object> item = new HashMap<>();
                item.put("name", name);
                item.put("price", price);
                item.put("quantity", quantity);
                item.put("shippingFee", shippingFee);
                item.put("total", totalWithShipping);
                item.put("index", productIndex);
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

        return "index"; // Thymeleaf template
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

    @GetMapping("/product/{index}")
    public String productController(@PathVariable String index, Model model) {
        model.addAttribute("contentType", "product");
        String[] parts = index.split("-");
        String type = parts[1];
        int idx = Integer.parseInt(parts[0]);
        String name;
        String description;
        String price;
        String category;
        switch (type) {
            case "A":
                name = AppConfig.bracelets[idx][0];
                description = AppConfig.bracelets[idx][1];
                price = AppConfig.bracelets[idx][2];
                category = "bracelets";
                break;
            case "B":
                name = AppConfig.earrings[idx][0];
                description = AppConfig.earrings[idx][1];
                price = AppConfig.earrings[idx][2];
                category = "earrings";
                break;
            case "C":
                name = AppConfig.necklaces[idx][0];
                description = AppConfig.necklaces[idx][1];
                price = AppConfig.necklaces[idx][2];
                category = "necklaces";
                break;
            case "D":
                name = AppConfig.rings[idx][0];
                description = AppConfig.rings[idx][1];
                price = AppConfig.rings[idx][2];
                category = "rings";
                break;
            default:
                name = "none";
                description = "none";
                price = "none";
                category = "none";
                break;
        }
        model.addAttribute("title", name);
        model.addAttribute("description", description);
        model.addAttribute("price", price);
        model.addAttribute("category", category);
        model.addAttribute("index", idx);
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
            String getOrdersSql = "SELECT id, order_date, total_amount, shipping_full_name, shipping_address, shipping_email, shipping_mobile_number FROM orders WHERE user_id = ? ORDER BY order_date DESC";
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

                Map<String, Object> order = new HashMap<>();
                order.put("orderId", orderId);
                order.put("orderDate", orderDate);
                order.put("totalAmount", String.format("₱ %.2f", totalAmount));
                order.put("shippingFullName", shippingFullName);
                order.put("shippingAddress", shippingAddress);
                order.put("shippingEmail", shippingEmail);
                order.put("shippingMobileNumber", shippingMobileNumber);
                order.put("status", "Processing");

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
}
