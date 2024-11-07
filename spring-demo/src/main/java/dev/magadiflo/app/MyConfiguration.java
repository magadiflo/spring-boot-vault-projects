package dev.magadiflo.app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "demo")
public class MyConfiguration {
    private String username;
    private String password;
    private String tokenApp;
}
