package com.example.purchases.controllers;

import com.example.purchases.services.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

    private final AuthorizationService _authorizationService;

    public MainController(AuthorizationService authorizationService) {
        _authorizationService = authorizationService;
    }

    @GetMapping(path = "/")
    public RedirectView main(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey) {
        if (_authorizationService.isLogin(sessionKey)) {
            return new RedirectView("/products");
        } else {
            return new RedirectView("/login");
        }
    }
}
