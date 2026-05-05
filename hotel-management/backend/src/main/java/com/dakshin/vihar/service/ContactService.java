package com.dakshin.vihar.service;

import com.dakshin.vihar.model.Contact;
import com.dakshin.vihar.repository.ContactRepository;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final EmailService emailService;

    public ContactService(ContactRepository contactRepository, EmailService emailService) {
        this.contactRepository = contactRepository;
        this.emailService = emailService;
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

        Contact savedContact = contactRepository.save(contact);
        try {
            emailService.sendAdminContactNotification(savedContact);
        } catch (MailException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not send contact email.", ex);
        }
        return savedContact;
    }
}

