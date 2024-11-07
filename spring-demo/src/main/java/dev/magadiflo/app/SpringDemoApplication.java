package dev.magadiflo.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(MyConfiguration.class)
@SpringBootApplication
public class SpringDemoApplication implements CommandLineRunner {

    private final MyConfiguration configuration;

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("----------------------------------------");
        log.info("Configuration properties");
        log.info("   demo.username is {}", configuration.getUsername());
        log.info("   demo.password is {}", configuration.getPassword());
        log.info("   demo.token-app is {}", configuration.getTokenApp());
        log.info("----------------------------------------");
    }
}
