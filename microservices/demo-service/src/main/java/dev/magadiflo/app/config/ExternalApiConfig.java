package dev.magadiflo.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiConfig {
    private String apiKey;
    private String username;
    private String dbPassword; //Nueva configuración en Vault (default)
    private String url; //Nueva configuración en el application.yml de demo-service
}
