package com.dakshin.vihar.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String email;

    @Column(name = "room_type", nullable = false)
    private String roomType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    public Booking() {}

    public Booking(String customerName, String email, String roomType, LocalDate checkIn, LocalDate checkOut) {
        this.customerName = customerName;
        this.email = email;
        this.roomType = roomType;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
}

