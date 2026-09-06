package com.wikmind.worker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "wikmind.ai")
public record AiProperties(
        Map<String, TaskConfiguration> tasks,
        Providers providers
) {

    public record TaskConfiguration(
            String provider,
            String model
    ) {}

    public record Providers(
            OpenAi openai,
            Ollama ollama
    ) {}

    public record OpenAi(
            String apiKey
    ) {}

    public record Ollama(
            String baseUrl
    ) {}
}