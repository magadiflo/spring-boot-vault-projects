package dev.magadiflo.app.controller;

import dev.magadiflo.app.config.ExternalApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/externals")
public class ExternalController {

    private final ExternalApiConfig configuration;

    @GetMapping
    public void getProperties() {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   external-api.api-key: {}", configuration.getApiKey());
        log.info("   external-api.username: {}", configuration.getUsername());

        log.info("   external-api.db-password: {}", configuration.getDbPassword());
        log.info("   external-api.url: {}", configuration.getUrl());
        log.info("----------------------------------------");
    }

}
