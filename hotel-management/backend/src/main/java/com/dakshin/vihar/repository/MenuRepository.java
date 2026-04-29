package com.dakshin.vihar.repository;

import com.dakshin.vihar.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuItem, Long> {
}

