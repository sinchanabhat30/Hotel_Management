package com.dakshin.vihar.service;

import com.dakshin.vihar.model.Booking;
import com.dakshin.vihar.repository.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.emailService = emailService;
    }

    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking payload is required.");
        }
        if (booking.getCustomerName() == null || booking.getCustomerName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "customerName is required.");
        }
        if (booking.getEmail() == null || booking.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is required.");
        }
        if (booking.getRoomType() == null || booking.getRoomType().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "roomType is required.");
        }
        if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkIn and checkOut are required.");
        }

        LocalDate checkIn = booking.getCheckIn();
        LocalDate checkOut = booking.getCheckOut();
        if (!checkOut.isAfter(checkIn)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkOut must be after checkIn.");
        }

        Booking savedBooking = bookingRepository.save(booking);
        emailService.sendBookingConfirmation(savedBooking);
        return savedBooking;
    }
}

