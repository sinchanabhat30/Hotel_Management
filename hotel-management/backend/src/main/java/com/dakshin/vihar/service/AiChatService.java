package com.dakshin.vihar.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AiChatService {
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final String openAiApiKey;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public AiChatService(Environment environment) {
        this.openAiApiKey = Optional.ofNullable(environment.getProperty("OPENAI_API_KEY"))
                .or(() -> Optional.ofNullable(environment.getProperty("openai.api.key")))
                .orElse("")
                .trim();
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public String ask(String message) {
        String trimmed = message == null ? "" : message.trim();
        if (trimmed.isEmpty()) {
            return "Please ask a question about rooms, menu, booking process, or contact details.";
        }

        if (!openAiApiKey.isBlank()) {
            try {
                return callOpenAi(trimmed);
            } catch (Exception ex) {
                return fallbackAnswer(trimmed);
            }
        }

        return fallbackAnswer(trimmed);
    }

    private String callOpenAi(String message) throws IOException, InterruptedException {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful hotel assistant for Dakshin Vihar. Answer questions about rooms, menu, booking process, and contact details in a concise, professional tone."),
                        Map.of("role", "user", "content", message)
                ),
                "max_tokens", 250,
                "temperature", 0.7
        );

        String body = objectMapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .timeout(Duration.ofSeconds(30))
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("OpenAI request failed with status " + response.statusCode());
        }

        Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new IOException("Unexpected OpenAI response structure.");
        }

        Map<String, Object> choice = choices.get(0);
        Map<String, Object> messageNode = (Map<String, Object>) choice.get("message");
        if (messageNode == null) {
            throw new IOException("OpenAI response missing message.");
        }

        Object content = messageNode.get("content");
        return content == null ? "I’m sorry, I could not generate a response right now." : content.toString().trim();
    }

    private String fallbackAnswer(String message) {
        String lower = message.toLowerCase();

        if (lower.contains("room") || lower.contains("stay") || lower.contains("ac") || lower.contains("executive") || lower.contains("deluxe")) {
            return "Our rooms are designed for comfort, with options such as AC Deluxe and Executive Double. You can view prices and availability on the Rooms section, then press Book to submit a reservation request.";
        }

        if (lower.contains("menu") || lower.contains("dish") || lower.contains("food") || lower.contains("coffee") || lower.contains("dosa")) {
            return "The menu features premium South Indian dishes, including dosas, filter coffee, and house specialties. The Menu section loads all items dynamically from our backend.";
        }

        if (lower.contains("book") || lower.contains("reservation") || lower.contains("check-in") || lower.contains("booking process")) {
            return "To book a room, choose a room type in the Rooms section and click Book. Then complete the form with your name, email, and stay dates. The backend validates availability before confirming your booking.";
        }

        if (lower.contains("contact") || lower.contains("phone") || lower.contains("email") || lower.contains("address")) {
            return "You can contact Dakshin Vihar by phone at +91 98765 43210 or by email at dakshinviharhotel@gmail.com. Our guest services are available between 9:00 AM and 11:00 PM.";
        }

        return "I can help with questions about rooms, menu, booking process, or contact details. Please ask one of those topics, and I’ll provide a concise answer.";
    }
}
