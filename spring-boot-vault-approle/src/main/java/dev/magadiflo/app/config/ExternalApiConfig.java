package dev.magadiflo.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiConfig {
    private String apiKey;
    private String token;
    private String username;

    private String dbPassword;
    private String dbUser;

    private String uri; // Obtenida del application.yml
}