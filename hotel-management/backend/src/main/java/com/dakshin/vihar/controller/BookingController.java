package com.dakshin.vihar.controller;

import com.dakshin.vihar.model.Booking;
import com.dakshin.vihar.service.BookingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }
}

