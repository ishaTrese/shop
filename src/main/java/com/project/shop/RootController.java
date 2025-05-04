package com.project.shop;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

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
        model.addAttribute("title", "Account");
        model.addAttribute("contentType", "account");
        return "index";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        model.addAttribute("title", "Cart");
        model.addAttribute("contentType", "cart");
        return "index";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("title", "Checkout");
        model.addAttribute("contentType", "checkout");
        return "index";
    }

    @GetMapping("/account/login")
    public String login(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("contentType", "login");
        return "index";
    }

    @GetMapping("/account/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Login");
        model.addAttribute("contentType", "signup");
        return "index";
    }
}
