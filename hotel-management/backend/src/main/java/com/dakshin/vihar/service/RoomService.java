package com.dakshin.vihar.service;

import com.dakshin.vihar.model.Room;
import com.dakshin.vihar.repository.BookingRepository;
import com.dakshin.vihar.repository.RoomRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        for (Room room : rooms) {
            long currentBookings = bookingRepository.countByRoomType(room.getType());
            int availableCount = 20 - (int) currentBookings;
            room.setAvailableCount(availableCount);
            room.setStatus(availableCount > 0 ? "Available" : "Booked");
        }
        return rooms;
    }
}

