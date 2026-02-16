package ru.practicum.shareit.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ShareItGatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(ShareItGatewayApp.class, args);
    }
}
