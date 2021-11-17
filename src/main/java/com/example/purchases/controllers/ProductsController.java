package com.example.purchases.controllers;

import com.example.purchases.dbService.DBService;
import com.example.purchases.dbService.entities.Product;
import com.example.purchases.dbService.entities.User;
import com.example.purchases.exceptions.ValidationException;
import com.example.purchases.models.ProductModel;
import com.example.purchases.services.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.purchases.extensions.StringExtensions.isNullOrEmpty;


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
        } catch (ValidationException exception) {
            model.addAttribute("error", exception.getMessage());
        }

        return new ModelAndView("products");
    }

    @PostMapping(path = "/addProduct")
    public ModelAndView addProduct(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                                   @RequestBody HashMap<String, String> data,
                                   Model model) {

        String description = data.get("description");
        ArrayList<String> errors = validateRequest(sessionKey, description);

        if (errors.isEmpty()) {
            try {
                int userId = _authorizationService.getUserId(sessionKey);
                User user = _dbService.getUser(userId);

                Product product = new Product(description, user);
                _dbService.addProduct(product);
            } catch (ValidationException exception) {
                errors.add(exception.getMessage());
            }
        }

        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
        }

        return new ModelAndView("products");
    }

    @PostMapping(path = "/updateProduct")
    public ModelAndView updateProduct(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                                      @RequestBody HashMap<String, String> data,
                                      Model model) {

        String description = data.get("description");
        ArrayList<String> errors = validateRequest(sessionKey, description);

        if (errors.isEmpty()) {
            try {
                ProductModel productModel = getProductModel(data, description);
                int userId = _authorizationService.getUserId(sessionKey);
                _dbService.updateProduct(userId, productModel);
            } catch (ValidationException exception) {
                errors.add(exception.getMessage());
            }
        }

        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
        }

        return new ModelAndView("products");
    }

    @PostMapping(path = "/deleteProduct")
    public ModelAndView deleteProduct(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                                      @RequestBody HashMap<String, String> data,
                                      Model model) {

        ArrayList<String> errors = new ArrayList<>();
        if (!_authorizationService.isLogin(sessionKey)) {
            errors.add("You are not authorized");
        }

        if (errors.isEmpty()) {
            try {
                int productId = getProductIdOrThrowIfNotValid(data);
                int userId = _authorizationService.getUserId(sessionKey);
                _dbService.deleteProduct(userId, productId);
            } catch (ValidationException exception) {
                errors.add(exception.getMessage());
            }
        }

        if (errors.size() > 0) {
            model.addAttribute("errors", errors);
        }

        return new ModelAndView("products");
    }


    private ProductModel getProductModel(HashMap<String, String> data, String description) throws ValidationException {
        int productId = getProductIdOrThrowIfNotValid(data);
        boolean isCompleted = Boolean.parseBoolean(data.get("isCompleted"));

        return new ProductModel(productId, description, isCompleted);
    }

    private int getProductIdOrThrowIfNotValid(HashMap<String, String> data) throws ValidationException {
        int productId;
        try {
            productId = Integer.parseInt(data.get("productId"));
        } catch (NumberFormatException exception) {
            productId = 0;
        }

        if (productId <= 0) {
            throw new ValidationException("Field 'productId' must be set");
        }
        return productId;
    }

    private ArrayList<String> validateRequest(String sessionKey, String description) {

        ArrayList<String> errors = new ArrayList<>();
        if (!_authorizationService.isLogin(sessionKey)) {
            errors.add("You are not authorized");
        }

        if (isNullOrEmpty(description)) {
            errors.add("Description can't be empty");
        }

        return errors;
    }
}
