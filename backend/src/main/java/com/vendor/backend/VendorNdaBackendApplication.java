package com.vendor.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main entry point for the Vendor NDA Tracker Spring Boot backend.
 *
 * This application exposes REST APIs that:
 *  - Accept vendor NDA text from clients
 *  - Delegate AI analysis to the Flask AI microservice
 *  - Return structured recommendations and reports
 *
 * @EnableAsync enables background async processing for AI calls
 */
@SpringBootApplication
@EnableAsync
public class VendorNdaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendorNdaBackendApplication.class, args);
    }
}
