package com.example.purchases.controllers;

import com.example.purchases.dbService.DBService;
import com.example.purchases.dbService.entities.Product;
import com.example.purchases.dbService.entities.User;
import com.example.purchases.exceptions.DBException;
import com.example.purchases.models.ProductModel;
import com.example.purchases.services.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;


@Controller
public class ProductsController {

    private final DBService _dbService;
    private final AuthorizationService _authorizationService;

    public ProductsController(DBService dbService,
                              AuthorizationService authorizationService) {
        _dbService = dbService;
        _authorizationService = authorizationService;
    }

    @GetMapping(path = "/products")
    public ModelAndView getProducts(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                                    Model model) {

        if (!_authorizationService.isLogin(sessionKey)) {
            return new ModelAndView("redirect:/error");
        }

        try {
            int userId = _authorizationService.getUserId(sessionKey);
            ProductModel[] products = _dbService.getProductsByUser(userId);
            model.addAttribute("products", products);
        } catch (DBException exception) {
            model.addAttribute("error", exception.getMessage());
        }

        return new ModelAndView("products");
    }

    @PostMapping(path = "/addProduct")
    public void addProduct(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                           @RequestParam HashMap<String, String> formData,
                           Model model) {

        String description = formData.get("description");
        ArrayList<String> errors = validateRequest(sessionKey, description);

        if (errors.isEmpty()) {
            try {
                int userId = _authorizationService.getUserId(sessionKey);
                User user = _dbService.getUser(userId);

                Product product = new Product(description, user);
                _dbService.addProduct(product);
            } catch (DBException exception) {
                errors.add(exception.getMessage());
            }
        }

        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
        }
    }

    @PostMapping(path = "/updateProduct")
    public void updateProduct(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                              @RequestParam HashMap<String, String> formData,
                              Model model) {

        String description = formData.get("description");
        ArrayList<String> errors = validateRequest(sessionKey, description);

        int productId;
        try {
            productId = Integer.parseInt(formData.get("productId"));
        } catch (NumberFormatException exception) {
            productId = 0;
        }

        if (productId <= 0) {
            errors.add("Field 'productId' must be set");
        }

        if (errors.isEmpty()) {
            try {
                int userId = _authorizationService.getUserId(sessionKey);
                _dbService.updateProduct(userId, productId, description);
            } catch (DBException exception) {
                errors.add(exception.getMessage());
            }
        }

        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
        }
    }

    private ArrayList<String> validateRequest(String sessionKey, String description) {

        ArrayList<String> errors = new ArrayList<>();
        if (!_authorizationService.isLogin(sessionKey)) {
            errors.add("You are not authorized");
        }

        if (description.isBlank()) {
            errors.add("Description can't be empty");
        }

        return errors;
    }
}
