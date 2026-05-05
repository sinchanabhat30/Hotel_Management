package com.dakshin.vihar.repository;

import com.dakshin.vihar.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByRoomTypeAndCheckInLessThanAndCheckOutGreaterThan(String roomType, LocalDate checkOut, LocalDate checkIn);

    boolean existsByRoomTypeAndCheckInLessThanEqualAndCheckOutGreaterThan(String roomType, LocalDate dateStart, LocalDate dateEnd);

    long countByRoomType(String roomType);

    long countByRoomTypeAndCheckInLessThanAndCheckOutGreaterThan(String roomType, LocalDate checkOut, LocalDate checkIn);

    long countByRoomTypeAndCheckInLessThanEqualAndCheckOutGreaterThan(String roomType, LocalDate dateStart, LocalDate dateEnd);
}

