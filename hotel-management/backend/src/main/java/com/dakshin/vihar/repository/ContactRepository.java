package com.dakshin.vihar.repository;

import com.dakshin.vihar.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}

