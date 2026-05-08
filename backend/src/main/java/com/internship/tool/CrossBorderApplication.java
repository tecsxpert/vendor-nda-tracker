package com.internship.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main entry point for the Cross-Border Data Transfer Manager Spring Boot backend.
 *
 * Exposes REST APIs that:
 *  - Accept cross-border data transfer details from clients
 *  - Delegate AI analysis to the Flask AI microservice (port 5000)
 *  - Return structured risk reports and recommendations
 *
 * @EnableAsync enables background async processing for AI calls
 */
@SpringBootApplication
@EnableAsync
public class CrossBorderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrossBorderApplication.class, args);
    }
}
