package com.eshop.bazar.controllers;

import com.eshop.bazar.domain.CustomUser;
import com.eshop.bazar.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    AuthService authService;

    @GetMapping("/register")
    public String showRegisterUI(Model model){

        model.addAttribute("customUser", new CustomUser());
        return "user_register";
    }

    @PostMapping("/register")
    public String registerUser(CustomUser user){

        authService.registerNewUser(user);
        return "redirect:/login";
    }
}
