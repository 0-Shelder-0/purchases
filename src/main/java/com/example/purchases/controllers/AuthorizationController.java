package com.example.purchases.controllers;

import com.example.purchases.exceptions.DBException;
import com.example.purchases.models.UserModel;
import com.example.purchases.services.AuthorizationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;

import static com.example.purchases.extensions.StringExtensions.isNullOrWhiteSpace;

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
    public RedirectView login(
            @CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
            @RequestParam HashMap<String, String> formData,
            Model model) {

        String login = formData.get("login");
        String password = formData.get("password");

        if (isNullOrWhiteSpace(login) || isNullOrWhiteSpace(password)) {
            model.addAttribute("error", "Login and/or password can't be empty");
        } else {
            UserModel user = new UserModel(login, password);
            try {
                _authorizationService.login(sessionKey, user);
                return new RedirectView("/index");
            } catch (DBException exception) {
                model.addAttribute("error", exception.getMessage());
            }
        }

        return null;
    }

    @PostMapping(path = "/signup")
    public RedirectView signUp(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey,
                               @RequestParam HashMap<String, String> formData,
                               Model model) {

        String login = formData.get("login");
        String password = formData.get("password");
        String email = formData.get("email");

        if (isNullOrWhiteSpace(login) || isNullOrWhiteSpace(password) || isNullOrWhiteSpace(email)) {
            model.addAttribute("error", "Login, email and password can't be empty");
        } else {
            UserModel user = new UserModel(login, password, email);
            try {
                _authorizationService.register(user);
                _authorizationService.login(sessionKey, user);
                return new RedirectView("/index");
            } catch (DBException exception) {
                model.addAttribute("error", exception.getMessage());
            }
        }

        return null;
    }

    @PostMapping(path = "/logout")
    public RedirectView logout(@CookieValue(value = "JSESSIONID", defaultValue = "") String sessionKey) {
        _authorizationService.logout(sessionKey);
        return new RedirectView("/login");
    }
}
