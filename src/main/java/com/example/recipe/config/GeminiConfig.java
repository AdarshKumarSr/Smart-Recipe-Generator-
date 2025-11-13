package com.example.recipe.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeminiConfig {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Bean
    public Client geminiClient() {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalStateException("❌ Missing gemini.api.key in application.properties");
        }

        // ✅ Create Gemini client directly with API key
        Client client = new Client.Builder()
                .apiKey(geminiApiKey)
                .build();

        System.out.println("✅ Gemini client initialized with direct API key.");
        return client;
    }
}
