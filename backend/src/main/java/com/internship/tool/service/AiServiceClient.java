package com.internship.tool.service;

import com.internship.tool.dto.DescribeResponse;
import com.internship.tool.dto.RecommendResponse;
import com.internship.tool.dto.ReportResponse;
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
 *   POST /describe        → plain-language description of data transfer
 *   POST /recommend       → actionable compliance recommendations
 *   POST /generate-report → full structured risk report
 *
 * All methods return null on failure (never throw to the caller).
 */
@Component
public class AiServiceClient {

    private final RestTemplate restTemplate;

    @Value("${ai.service.base-url:http://localhost:5000}")
    private String aiServiceBaseUrl;

    public AiServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DescribeResponse callDescribeAPI(String input) {
        String url = aiServiceBaseUrl + "/describe";
        try {
            HttpEntity<String> request = buildRequest(input);
            ResponseEntity<DescribeResponse> response =
                    restTemplate.postForEntity(url, request, DescribeResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            System.err.println("[AiServiceClient] /describe failed: " + e.getMessage());
        }
        return null;
    }

    public RecommendResponse callRecommendAPI(String input) {
        String url = aiServiceBaseUrl + "/recommend";
        try {
            HttpEntity<String> request = buildRequest(input);
            ResponseEntity<RecommendResponse> response =
                    restTemplate.postForEntity(url, request, RecommendResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            System.err.println("[AiServiceClient] /recommend failed: " + e.getMessage());
        }
        return null;
    }

    public ReportResponse callGenerateReportAPI(String input) {
        String url = aiServiceBaseUrl + "/generate-report";
        try {
            HttpEntity<String> request = buildRequest(input);
            ResponseEntity<ReportResponse> response =
                    restTemplate.postForEntity(url, request, ReportResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (RestClientException e) {
            System.err.println("[AiServiceClient] /generate-report failed: " + e.getMessage());
        }
        return null;
    }

    private HttpEntity<String> buildRequest(String input) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        String escaped = input.replace("\\", "\\\\").replace("\"", "\\\"");
        String body = "{\"input\": \"" + escaped + "\"}";
        return new HttpEntity<>(body, headers);
    }
}
