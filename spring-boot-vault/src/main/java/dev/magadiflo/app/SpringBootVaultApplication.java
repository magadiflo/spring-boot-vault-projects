package dev.magadiflo.app;

import dev.magadiflo.app.config.ExternalApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(ExternalApiConfig.class)
@SpringBootApplication
public class SpringBootVaultApplication implements CommandLineRunner {

    private final ExternalApiConfig configuration;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootVaultApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   external-api.api-key: {}", configuration.getApiKey());
        log.info("   external-api.token: {}", configuration.getToken());
        log.info("   external-api.username: {}", configuration.getUsername());
        log.info("   external-api.db-password: {}", configuration.getDbPassword());
        log.info("   external-api.db-user: {}", configuration.getDbUser());
        log.info("   external-api.uri: {}", configuration.getUri());
        log.info("----------------------------------------");
    }
}
