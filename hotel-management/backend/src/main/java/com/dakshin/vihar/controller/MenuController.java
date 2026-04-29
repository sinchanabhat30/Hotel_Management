package com.dakshin.vihar.controller;

import com.dakshin.vihar.model.MenuItem;
import com.dakshin.vihar.service.MenuService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/menu")
    public List<MenuItem> getMenu() {
        return menuService.getAllMenu();
    }
}

