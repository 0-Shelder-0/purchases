package com.example.purchases.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

    @GetMapping(path = "/")
    public RedirectView index() {
        return new RedirectView("/login");
    }
}
