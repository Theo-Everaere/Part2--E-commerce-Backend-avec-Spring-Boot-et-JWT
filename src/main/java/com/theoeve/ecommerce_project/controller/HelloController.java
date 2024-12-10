package com.theoeve.ecommerce_project.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    
    @GetMapping("/")
    public String greetings(HttpServletRequest request) {
        return "Hello " + request.getSession().getId();
    }
}
