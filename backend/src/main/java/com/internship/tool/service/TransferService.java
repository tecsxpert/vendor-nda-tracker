package com.internship.tool.service;

import com.internship.tool.dto.DescribeResponse;
import com.internship.tool.dto.RecommendResponse;
import com.internship.tool.dto.ReportResponse;
import com.internship.tool.dto.TransferRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Core business logic service for Cross-Border Data Transfer analysis.
 *
 * Responsibilities:
 *  1. Validate and sanitize incoming transfer text
 *  2. Delegate calls to AiServiceClient (Flask microservice)
 *  3. Provide safe fallback responses when AI service is unavailable
 *  4. Support async background processing for fire-and-forget scenarios
 *
 * All public methods never throw exceptions — they return a valid response or fallback.
 */
@Service
public class TransferService {

    private final AiServiceClient aiServiceClient;

    public TransferService(AiServiceClient aiServiceClient) {
        this.aiServiceClient = aiServiceClient;
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Describe (synchronous)
    // ─────────────────────────────────────────────────────────────

    public DescribeResponse describeTransfer(TransferRequest request) {
        String input = sanitize(request.getInput());
        if (input.isBlank()) {
            return new DescribeResponse("Input text was empty after sanitization.", true,
                    java.time.Instant.now().toString());
        }
        System.out.println("[TransferService] Calling AI /describe, input length=" + input.length());
        DescribeResponse response = aiServiceClient.callDescribeAPI(input);
        if (response != null) return response;
        System.err.println("[TransferService] AI /describe returned null, using fallback.");
        return new DescribeResponse(
                "AI service is temporarily unavailable. Please review the transfer details manually.",
                true, java.time.Instant.now().toString());
    }

    // ─────────────────────────────────────────────────────────────
    // 2. Recommend (synchronous)
    // ─────────────────────────────────────────────────────────────

    public RecommendResponse getRecommendations(TransferRequest request) {
        String input = sanitize(request.getInput());
        if (input.isBlank()) return buildFallbackRecommendations();
        System.out.println("[TransferService] Calling AI /recommend, input length=" + input.length());
        RecommendResponse response = aiServiceClient.callRecommendAPI(input);
        if (response != null) return response;
        System.err.println("[TransferService] AI /recommend returned null, using fallback.");
        return buildFallbackRecommendations();
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Generate Report (synchronous)
    // ─────────────────────────────────────────────────────────────

    public ReportResponse generateReport(TransferRequest request) {
        String input = sanitize(request.getInput());
        if (input.isBlank()) return buildFallbackReport();
        System.out.println("[TransferService] Calling AI /generate-report, input length=" + input.length());
        ReportResponse response = aiServiceClient.callGenerateReportAPI(input);
        if (response != null) return response;
        System.err.println("[TransferService] AI /generate-report returned null, using fallback.");
        return buildFallbackReport();
    }

    // ─────────────────────────────────────────────────────────────
    // 4. Async background processing (fire-and-forget)
    // ─────────────────────────────────────────────────────────────

    @Async("aiTaskExecutor")
    public void processTransferAsync(String transferDetails) {
        if (transferDetails == null || transferDetails.isBlank()) {
            System.out.println("[TransferService] Async: skipping — input is empty");
            return;
        }
        System.out.println("[TransferService] Async: starting background AI processing...");
        RecommendResponse result = aiServiceClient.callRecommendAPI(sanitize(transferDetails));
        if (result != null && result.getRecommendations() != null) {
            System.out.println("[TransferService] Async: received " +
                    result.getRecommendations().size() + " recommendations.");
        } else {
            System.err.println("[TransferService] Async: AI result was null or empty.");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────

    private String sanitize(String input) {
        if (input == null) return "";
        return input.strip();
    }

    private RecommendResponse buildFallbackRecommendations() {
        RecommendResponse response = new RecommendResponse();
        response.setIsFallback(true);
        response.setGeneratedAt(java.time.Instant.now().toString());
        response.setRecommendations(List.of(
                new RecommendResponse.Recommendation("Review", "Review the transfer agreement and applicable regulations."),
                new RecommendResponse.Recommendation("Consult", "Consult a data protection officer before proceeding."),
                new RecommendResponse.Recommendation("Verify", "Verify the destination country's data protection laws.")
        ));
        return response;
    }

    private ReportResponse buildFallbackReport() {
        ReportResponse response = new ReportResponse();
        response.setIsFallback(true);
        response.setCached(false);
        response.setGeneratedAt(java.time.Instant.now().toString());

        ReportResponse.Report report = new ReportResponse.Report();
        report.setTitle("Risk Report Unavailable");
        report.setSummary("AI service is temporarily unavailable.");
        report.setOverview("Please review the data transfer details manually.");
        report.setKeyItems(List.of("Review all data transfer clauses", "Check GDPR/CCPA compliance"));
        report.setRecommendations(List.of("Consult a legal expert", "Engage a data protection officer"));
        report.setRiskLevel("UNKNOWN");
        response.setReport(report);

        return response;
    }
}
