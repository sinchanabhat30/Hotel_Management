package com.dakshin.vihar.service;

import com.dakshin.vihar.model.Contact;
import com.dakshin.vihar.repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact createContact(Contact contact) {
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact payload is required.");
        }
        if (contact.getName() == null || contact.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required.");
        }
        if (contact.getEmail() == null || contact.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is required.");
        }
        if (contact.getMessage() == null || contact.getMessage().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message is required.");
        }

        return contactRepository.save(contact);
    }
}

