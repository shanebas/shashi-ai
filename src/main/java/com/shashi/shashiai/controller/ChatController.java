package com.shashi.shashiai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Handles a simple GET chat request with a message query parameter.
     *
     * @param message the user's input message
     * @return the AI-generated response as a plain string
     */
    @GetMapping
    public String chat(@RequestParam String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * Handles a POST chat request with the message in the request body.
     *
     * @param message the user's input message
     * @return the AI-generated response as a plain string
     */
    @PostMapping
    public String chatPost(@RequestBody String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * Handles a POST chat request with a system prompt and user message.
     * Useful for setting context/persona before chatting.
     *
     * @param request the chat request containing system prompt and user message
     * @return the AI-generated response as a plain string
     */
    @PostMapping("/with-system")
    public String chatWithSystem(@RequestBody ChatRequest request) {
        return chatClient
                .prompt()
                .system(request.systemPrompt())
                .user(request.userMessage())
                .call()
                .content();
    }

    /**
     * Simple record to hold a chat request with a system prompt and user message.
     */
    public record ChatRequest(String systemPrompt, String userMessage) {
    }

}
