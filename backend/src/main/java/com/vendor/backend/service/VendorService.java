package com.vendor.backend.service;

import com.vendor.backend.dto.DescribeResponse;
import com.vendor.backend.dto.NdaAnalysisRequest;
import com.vendor.backend.dto.RecommendResponse;
import com.vendor.backend.dto.ReportResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Core business logic service for Vendor NDA analysis.
 *
 * Responsibilities:
 *  1. Validate and sanitize incoming NDA text
 *  2. Delegate calls to AiServiceClient (Flask microservice)
 *  3. Provide fallback responses when the AI service is unavailable
 *  4. Support async background processing for fire-and-forget scenarios
 *
 * All public methods are designed to never throw exceptions to the caller —
 * they either return a valid response or a safe fallback.
 */
@Service
public class VendorService {

    private final AiServiceClient aiServiceClient;

    public VendorService(AiServiceClient aiServiceClient) {
        this.aiServiceClient = aiServiceClient;
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Describe NDA (synchronous - client waits for response)
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates a plain-language description of the vendor NDA.
     *
     * @param request validated NDA analysis request
     * @return AI-generated description, or fallback if AI is unavailable
     */
    public DescribeResponse describeNda(NdaAnalysisRequest request) {
        String input = sanitize(request.getInput());

        if (input.isBlank()) {
            return buildFallbackDescription("Input text was empty after sanitization.");
        }

        System.out.println("[VendorService] Calling AI /describe for input length: " + input.length());
        DescribeResponse response = aiServiceClient.callDescribeAPI(input);

        if (response != null) {
            System.out.println("[VendorService] AI /describe succeeded. Fallback=" + response.isIsFallback());
            return response;
        }

        System.err.println("[VendorService] AI /describe returned null, using fallback.");
        return buildFallbackDescription("AI service is temporarily unavailable. Please review the NDA manually.");
    }

    // ─────────────────────────────────────────────────────────────
    // 2. Get Recommendations (synchronous)
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates actionable recommendations based on the vendor NDA text.
     *
     * @param request validated NDA analysis request
     * @return AI-generated recommendations, or fallback if AI is unavailable
     */
    public RecommendResponse getRecommendations(NdaAnalysisRequest request) {
        String input = sanitize(request.getInput());

        if (input.isBlank()) {
            return buildFallbackRecommendations();
        }

        System.out.println("[VendorService] Calling AI /recommend for input length: " + input.length());
        RecommendResponse response = aiServiceClient.callRecommendAPI(input);

        if (response != null) {
            System.out.println("[VendorService] AI /recommend succeeded. Fallback=" + response.isIsFallback());
            return response;
        }

        System.err.println("[VendorService] AI /recommend returned null, using fallback.");
        return buildFallbackRecommendations();
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Generate Full NDA Risk Report (synchronous)
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates a full structured NDA risk report.
     *
     * @param request validated NDA analysis request
     * @return AI-generated report, or fallback if AI is unavailable
     */
    public ReportResponse generateReport(NdaAnalysisRequest request) {
        String input = sanitize(request.getInput());

        if (input.isBlank()) {
            return buildFallbackReport();
        }

        System.out.println("[VendorService] Calling AI /generate-report for input length: " + input.length());
        ReportResponse response = aiServiceClient.callGenerateReportAPI(input);

        if (response != null) {
            System.out.println("[VendorService] AI /generate-report succeeded. Fallback=" + response.isIsFallback());
            return response;
        }

        System.err.println("[VendorService] AI /generate-report returned null, using fallback.");
        return buildFallbackReport();
    }

    // ─────────────────────────────────────────────────────────────
    // 4. Async Background Processing (fire-and-forget)
    // ─────────────────────────────────────────────────────────────

    /**
     * Asynchronously processes NDA recommendations in the background.
     * Used for fire-and-forget use cases where the client doesn't need
     * to wait for the AI result (e.g., after saving a vendor record).
     *
     * @param vendorDetails raw vendor/NDA text
     */
    @Async("aiTaskExecutor")
    public void processRecommendationsAsync(String vendorDetails) {
        if (vendorDetails == null || vendorDetails.isBlank()) {
            System.out.println("[VendorService] Async: Skipping AI call - input is empty");
            return;
        }

        System.out.println("[VendorService] Async: Starting background AI processing...");

        // Simulate saving basic vendor data first
        System.out.println("[VendorService] Async: Vendor data saved: "
                + vendorDetails.substring(0, Math.min(50, vendorDetails.length())) + "...");

        // Call AI in background
        RecommendResponse result = aiServiceClient.callRecommendAPI(sanitize(vendorDetails));

        if (result != null && result.getRecommendations() != null) {
            System.out.println("[VendorService] Async: Received " + result.getRecommendations().size()
                    + " AI recommendations.");
            // In a real app: save recommendations to DB here
        } else {
            System.err.println("[VendorService] Async: AI result was null or empty, skipping DB update.");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────

    /**
     * Sanitizes input by stripping leading/trailing whitespace.
     * Additional sanitization (HTML encoding, prompt injection detection)
     * is handled by the Flask AI service.
     */
    private String sanitize(String input) {
        if (input == null) return "";
        return input.strip();
    }

    private DescribeResponse buildFallbackDescription(String message) {
        return new DescribeResponse(
                message,
                true,
                java.time.Instant.now().toString()
        );
    }

    private RecommendResponse buildFallbackRecommendations() {
        RecommendResponse response = new RecommendResponse();
        response.setIsFallback(true);
        response.setGeneratedAt(java.time.Instant.now().toString());
        response.setRecommendations(List.of(
                new RecommendResponse.Recommendation("Review", "Please review the vendor NDA carefully."),
                new RecommendResponse.Recommendation("Consult", "Consult a legal expert before signing.")
        ));
        return response;
    }

    private ReportResponse buildFallbackReport() {
        ReportResponse response = new ReportResponse();
        response.setIsFallback(true);
        response.setCached(false);
        response.setGeneratedAt(java.time.Instant.now().toString());

        ReportResponse.Report report = new ReportResponse.Report();
        report.setTitle("NDA Report Unavailable");
        report.setSummary("AI service is temporarily unavailable.");
        report.setOverview("Please review the NDA manually.");
        report.setKeyItems(List.of("Review all clauses carefully"));
        report.setRecommendations(List.of("Consult a legal expert"));
        response.setReport(report);

        return response;
    }
}
