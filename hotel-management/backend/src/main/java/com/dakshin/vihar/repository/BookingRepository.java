package com.dakshin.vihar.repository;

import com.dakshin.vihar.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

