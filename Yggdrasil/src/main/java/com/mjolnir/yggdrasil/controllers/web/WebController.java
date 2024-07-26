package com.mjolnir.yggdrasil.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class WebController {

    @GetMapping("/Yggdrasil")
    public String index(Model model) {
        model.addAttribute("date", LocalDate.now());
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

}
