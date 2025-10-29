package com.alea.pokemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AleaBackendTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(AleaBackendTestApplication.class, args);
    }
}
