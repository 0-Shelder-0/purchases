package com.example.purchases.controllers;

import com.example.purchases.dbService.DBService;
import com.example.purchases.dbService.entities.Product;
import com.example.purchases.dbService.entities.User;
import com.example.purchases.exceptions.DBException;
import com.example.purchases.models.ProductModel;
import com.example.purchases.services.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

    private final AuthorizationService _authorizationService;
    private final DBService _dbService;

    public MainController(AuthorizationService authorizationService,
                          DBService dbService) {
        _authorizationService = authorizationService;
        _dbService = dbService;
    }

    @GetMapping(path = "/")
    public RedirectView main(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey) throws DBException {
        if (_authorizationService.isLogin(sessionKey)) {
            User user = _dbService.getUser(1);
            Product product = new Product();
            product.setDescription("descr");
            product.setUser(user);
            _dbService.addProduct(new ProductModel(product));
            return new RedirectView("/index");
        } else {
            return new RedirectView("/login");
        }
    }

    @GetMapping(path = "/index")
    public String index() {
        return "index";
    }
}
