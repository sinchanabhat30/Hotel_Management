package com.dakshin.vihar.service;

import com.dakshin.vihar.model.PartyHallBooking;
import com.dakshin.vihar.repository.PartyHallBookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class PartyHallBookingService {
    private final PartyHallBookingRepository partyHallBookingRepository;
    private final EmailService emailService;

    public PartyHallBookingService(PartyHallBookingRepository partyHallBookingRepository, EmailService emailService) {
        this.partyHallBookingRepository = partyHallBookingRepository;
        this.emailService = emailService;
    }

    public PartyHallBooking createPartyHallBooking(PartyHallBooking booking) {
        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking payload is required.");
        }
        if (booking.getFullName() == null || booking.getFullName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Full name is required.");
        }
        if (booking.getEmail() == null || booking.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required.");
        }
        if (booking.getPhoneNumber() == null || booking.getPhoneNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number is required.");
        }
        if (booking.getEventType() == null || booking.getEventType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event type is required.");
        }
        if (booking.getEventDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event date is required.");
        }
        if (booking.getTimeSlot() == null || booking.getTimeSlot().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time slot is required.");
        }
        if (booking.getNumberOfGuests() == null || booking.getNumberOfGuests() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of guests is required and must be greater than 0.");
        }

        // Check if the selected date and time slot is available
        if (partyHallBookingRepository.existsByEventDateAndTimeSlot(booking.getEventDate(), booking.getTimeSlot())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Selected slot is already booked. Please choose another time.");
        }

        // Validate event date is not in the past
        if (booking.getEventDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event date cannot be in the past.");
        }

        PartyHallBooking savedBooking = partyHallBookingRepository.save(booking);
        emailService.sendPartyHallBookingConfirmation(savedBooking);
        emailService.sendAdminPartyHallBookingNotification(savedBooking);
        return savedBooking;
    }
}