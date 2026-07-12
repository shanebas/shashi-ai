package com.shashi.shashiai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient ollamaChatClient;
    private final ChatClient openAiChatClient;

    public ChatController(
            @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
            @Qualifier("openAiChatClient") ChatClient openAiChatClient) {
        this.ollamaChatClient = ollamaChatClient;
        this.openAiChatClient = openAiChatClient;
    }

    private ChatClient getChatClient(String provider) {
        if ("openai".equalsIgnoreCase(provider)) {
            return openAiChatClient;
        }
        return ollamaChatClient;
    }

    /**
     * Handles a simple GET chat request with a message query parameter.
     *
     * @param message the user's input message
     * @param provider the AI provider to use ("ollama" or "openai")
     * @return the AI-generated response as a plain string
     */
    @GetMapping
    public String chat(
            @RequestParam String message,
            @RequestParam(required = false, defaultValue = "ollama") String provider) {
        return getChatClient(provider)
                .prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * Handles a POST chat request with the message in the request body.
     *
     * @param message the user's input message
     * @param provider the AI provider to use ("ollama" or "openai")
     * @return the AI-generated response as a plain string
     */
    @PostMapping
    public String chatPost(
            @RequestBody String message,
            @RequestParam(required = false, defaultValue = "ollama") String provider) {
        return getChatClient(provider)
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
     * @param provider the AI provider to use ("ollama" or "openai")
     * @return the AI-generated response as a plain string
     */
    @PostMapping("/with-system")
    public String chatWithSystem(
            @RequestBody ChatRequest request,
            @RequestParam(required = false, defaultValue = "ollama") String provider) {
        return getChatClient(provider)
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
