package com.dakshin.vihar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/party-hall")
    public String partyHall() {
        return "forward:/party-hall.html";
    }
}
