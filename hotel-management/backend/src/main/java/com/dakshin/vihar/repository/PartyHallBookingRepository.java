package com.dakshin.vihar.repository;

import com.dakshin.vihar.model.PartyHallBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PartyHallBookingRepository extends JpaRepository<PartyHallBooking, Long> {
    boolean existsByEventDateAndTimeSlot(LocalDate eventDate, String timeSlot);
}