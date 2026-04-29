package com.dakshin.vihar.controller;

import com.dakshin.vihar.model.Contact;
import com.dakshin.vihar.service.ContactService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contact")
    public Contact createContact(@RequestBody Contact contact) {
        return contactService.createContact(contact);
    }
}

