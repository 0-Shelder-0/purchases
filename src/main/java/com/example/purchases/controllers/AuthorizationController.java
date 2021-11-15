package com.example.purchases.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizationController {

    @GetMapping(path = "/login")
    public String login(Model model) {
        return "login";
    }
}
