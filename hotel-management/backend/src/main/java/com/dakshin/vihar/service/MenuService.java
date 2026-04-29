package com.dakshin.vihar.service;

import com.dakshin.vihar.model.MenuItem;
import com.dakshin.vihar.repository.MenuRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuItem> getAllMenu() {
        return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}

