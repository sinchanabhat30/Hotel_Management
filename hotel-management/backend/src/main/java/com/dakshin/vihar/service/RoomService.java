package com.dakshin.vihar.service;

import com.dakshin.vihar.model.Room;
import com.dakshin.vihar.repository.RoomRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}

