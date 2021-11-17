package com.example.purchases.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorsController implements ErrorController {

    @GetMapping(path = "/error")
    public String getErrorPage(ModelMap model) {
        model.addAttribute("a", "a");
        return "error";
    }
}
