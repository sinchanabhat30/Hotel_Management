package com.dakshin.vihar.controller;

import com.dakshin.vihar.model.PartyHallBooking;
import com.dakshin.vihar.service.PartyHallBookingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PartyHallController {
    private final PartyHallBookingService partyHallBookingService;

    public PartyHallController(PartyHallBookingService partyHallBookingService) {
        this.partyHallBookingService = partyHallBookingService;
    }

    @PostMapping("/party-hall-bookings")
    public PartyHallBooking createPartyHallBooking(@RequestBody PartyHallBooking booking) {
        return partyHallBookingService.createPartyHallBooking(booking);
    }
}