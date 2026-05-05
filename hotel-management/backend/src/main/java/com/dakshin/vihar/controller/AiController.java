package com.dakshin.vihar.controller;

import com.dakshin.vihar.service.AiChatService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AiController {
    private final AiChatService aiChatService;

    public AiController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/ai/chat")
    public AiChatResponse chat(@RequestBody AiChatRequest request) {
        if (request == null || request.message() == null || request.message().isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Please provide a chat message.");
        }

        String reply = aiChatService.ask(request.message());
        return new AiChatResponse(reply);
    }

    public static record AiChatRequest(String message) {
    }

    public static record AiChatResponse(String reply) {
    }
}
