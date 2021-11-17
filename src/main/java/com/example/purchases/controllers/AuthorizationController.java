package com.example.purchases.controllers;

import com.example.purchases.exceptions.ValidationException;
import com.example.purchases.models.UserModel;
import com.example.purchases.services.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.purchases.extensions.StringExtensions.isNullOrEmpty;

@Controller
public class AuthorizationController {

    private final AuthorizationService _authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        _authorizationService = authorizationService;
    }

    @GetMapping(path = "/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping(path = "/signup")
    public String getSignUpPage() {
        return "signup";
    }

    @PostMapping(path = "/login")
    public RedirectView login(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                              @RequestParam HashMap<String, String> formData,
                              Model model) {

        ArrayList<String> errors = new ArrayList<>();
        if (_authorizationService.isLogin(sessionKey)) {
            errors.add("You are already logged in");
        }

        String login = formData.get("login");
        String password = formData.get("password");
        if (isNullOrEmpty(login) || isNullOrEmpty(password)) {
            errors.add("Login and/or password can't be empty");
        }

        if (errors.isEmpty()) {
            UserModel user = new UserModel(login, password);
            try {
                _authorizationService.login(sessionKey, user);
                return new RedirectView("/products");
            } catch (ValidationException exception) {
                errors.add(exception.getMessage());
            }
        }
        model.addAttribute("errors", errors);

        return null;
    }

    @PostMapping(path = "/signup")
    public RedirectView signUp(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                               @RequestParam HashMap<String, String> formData,
                               Model model) {

        ArrayList<String> errors = new ArrayList<>();
        if (_authorizationService.isLogin(sessionKey)) {
            errors.add("You are already logged in");
        }

        String login = formData.get("login");
        String password = formData.get("password");
        String email = formData.get("email");
        if (isNullOrEmpty(login) || isNullOrEmpty(password) || isNullOrEmpty(email)) {
            errors.add("Login, email and password can't be empty");
        }

        if (errors.isEmpty()) {
            UserModel user = new UserModel(login, password, email);
            try {
                _authorizationService.register(user);
                _authorizationService.login(sessionKey, user);
                return new RedirectView("/products");
            } catch (ValidationException exception) {
                errors.add(exception.getMessage());
            }
        }
        model.addAttribute("errors", errors);

        return null;
    }

    @PostMapping(path = "/logout")
    public RedirectView logout(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey) {
        _authorizationService.logout(sessionKey);
        return new RedirectView("/login");
    }
}
