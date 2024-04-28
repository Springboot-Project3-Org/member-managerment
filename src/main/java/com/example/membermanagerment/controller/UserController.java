package com.example.membermanagerment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/login")
    public String Login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }
}
