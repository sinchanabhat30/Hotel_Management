package com.dakshin.vihar.controller;

import com.dakshin.vihar.model.Room;
import com.dakshin.vihar.service.RoomService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public List<Room> getRooms() {
        return roomService.getAllRooms();
    }
}

