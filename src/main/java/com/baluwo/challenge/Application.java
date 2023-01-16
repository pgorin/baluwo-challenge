package com.baluwo.challenge;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.Clock;

import static java.time.ZoneOffset.UTC;

@SpringBootApplication
@ComponentScan("com.baluwo.challenge")
public class Application {

    @Bean
    public static Clock clock() {
        return Clock.system(UTC);
    }

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}