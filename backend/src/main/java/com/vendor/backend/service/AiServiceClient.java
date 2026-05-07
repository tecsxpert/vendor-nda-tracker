package com.vendor.backend.service;

import com.vendor.backend.dto.DescribeResponse;
import com.vendor.backend.dto.RecommendResponse;
import com.vendor.backend.dto.ReportResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * HTTP client component for communicating with the Flask AI microservice.
 *
 * Calls three Flask endpoints:
 *   POST /describe        → generates plain-language NDA description
 *   POST /recommend       → generates actionable recommendations
 *   POST /generate-report → generates full structured NDA risk report
 *
 * Uses Spring's RestTemplate (configured as a shared bean in AppConfig).
 * All methods return null-safe values and log failures without throwing.
 */
@Component
public class AiServiceClient {

    private final RestTemplate restTemplate;

    /**
     * Base URL of the Flask AI service.
     * Defaults to localhost:5000 but can be overridden via application.properties.
     */
    @Value("${ai.service.base-url:http://127.0.0.1:5000}")
    private String aiServiceBaseUrl;

    public AiServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ─────────────────────────────────────────────────────────────
    // 1. /describe — plain-language NDA description
    // ─────────────────────────────────────────────────────────────

    /**
     * Calls the Flask /describe endpoint and returns the structured response.
     *
     * @param input raw NDA text
     * @return DescribeResponse from AI, or null if the call fails
     */
    public DescribeResponse callDescribeAPI(String input) {
        String url = aiServiceBaseUrl + "/describe";
        try {
            HttpEntity<String> request = buildRequest(input);
            ResponseEntity<DescribeResponse> response = restTemplate.postForEntity(
                    url, request, DescribeResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            System.err.println("[AiServiceClient] /describe call failed: " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────
    // 2. /recommend — actionable recommendations
    // ─────────────────────────────────────────────────────────────

    /**
     * Calls the Flask /recommend endpoint and returns actionable recommendations.
     *
     * @param input raw NDA text
     * @return RecommendResponse from AI, or null if the call fails
     */
    public RecommendResponse callRecommendAPI(String input) {
        String url = aiServiceBaseUrl + "/recommend";
        try {
            HttpEntity<String> request = buildRequest(input);
            ResponseEntity<RecommendResponse> response = restTemplate.postForEntity(
                    url, request, RecommendResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            System.err.println("[AiServiceClient] /recommend call failed: " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────
    // 3. /generate-report — full structured NDA risk report
    // ─────────────────────────────────────────────────────────────

    /**
     * Calls the Flask /generate-report endpoint and returns a full NDA risk report.
     *
     * @param input raw NDA text
     * @return ReportResponse from AI, or null if the call fails
     */
    public ReportResponse callGenerateReportAPI(String input) {
        String url = aiServiceBaseUrl + "/generate-report";
        try {
            HttpEntity<String> request = buildRequest(input);
            ResponseEntity<ReportResponse> response = restTemplate.postForEntity(
                    url, request, ReportResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            System.err.println("[AiServiceClient] /generate-report call failed: " + e.getMessage());
        }
        return null;
    }

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────

    /**
     * Builds a JSON HTTP request entity containing the input text.
     * Uses proper JSON escaping to handle quotes in the input.
     */
    private HttpEntity<String> buildRequest(String input) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        // Safely escape input for JSON embedding
        String escapedInput = input.replace("\\", "\\\\").replace("\"", "\\\"");
        String body = "{\"input\": \"" + escapedInput + "\"}";

        return new HttpEntity<>(body, headers);
    }
}
