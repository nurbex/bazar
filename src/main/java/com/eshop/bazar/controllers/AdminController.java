package com.eshop.bazar.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Properties;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String getAdminMainPage(Authentication auth){
        Properties pros = System.getProperties();
        pros.list(System.out);
        return "admin_main_page";
    }
}
