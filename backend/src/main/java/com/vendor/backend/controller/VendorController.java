package com.vendor.backend.controller;

import com.vendor.backend.dto.DescribeResponse;
import com.vendor.backend.dto.NdaAnalysisRequest;
import com.vendor.backend.dto.RecommendResponse;
import com.vendor.backend.dto.ReportResponse;
import com.vendor.backend.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing Vendor NDA analysis endpoints.
 *
 * Base URL: /vendor
 *
 * Endpoints:
 *  POST /vendor/describe         → AI plain-language description
 *  POST /vendor/recommend        → AI actionable recommendations
 *  POST /vendor/generate-report  → AI full NDA risk report
 *  POST /vendor/create           → async fire-and-forget vendor creation
 *
 * All endpoints accept JSON with { "input": "NDA text..." }
 * Input validation is performed via @Valid annotation.
 */
@RestController
@RequestMapping("/vendor")
@CrossOrigin(origins = "*")  // Allow requests from React frontend
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // ─────────────────────────────────────────────────────────────
    // POST /vendor/describe
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates a plain-language description of the vendor NDA.
     *
     * Example request:
     *   POST /vendor/describe
     *   { "input": "This NDA restricts the vendor from sharing confidential data for 2 years." }
     *
     * @param request validated NDA input from client
     * @return 200 OK with DescribeResponse (or fallback if AI unavailable)
     */
    @PostMapping("/describe")
    public ResponseEntity<DescribeResponse> describe(@Valid @RequestBody NdaAnalysisRequest request) {
        System.out.println("[VendorController] POST /vendor/describe received");
        DescribeResponse response = vendorService.describeNda(request);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────
    // POST /vendor/recommend
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates actionable recommendations for the vendor NDA.
     *
     * Example request:
     *   POST /vendor/recommend
     *   { "input": "Vendor NDA includes unlimited liability clause and no exit terms." }
     *
     * @param request validated NDA input from client
     * @return 200 OK with RecommendResponse (or fallback)
     */
    @PostMapping("/recommend")
    public ResponseEntity<RecommendResponse> recommend(@Valid @RequestBody NdaAnalysisRequest request) {
        System.out.println("[VendorController] POST /vendor/recommend received");
        RecommendResponse response = vendorService.getRecommendations(request);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────
    // POST /vendor/generate-report
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates a full structured NDA risk report.
     *
     * Example request:
     *   POST /vendor/generate-report
     *   { "input": "Vendor NDA with 3 year confidentiality period, unlimited liability, no exit clause." }
     *
     * @param request validated NDA input from client
     * @return 200 OK with ReportResponse containing full risk assessment (or fallback)
     */
    @PostMapping("/generate-report")
    public ResponseEntity<ReportResponse> generateReport(@Valid @RequestBody NdaAnalysisRequest request) {
        System.out.println("[VendorController] POST /vendor/generate-report received");
        ReportResponse response = vendorService.generateReport(request);
        return ResponseEntity.ok(response);
    }

    // ─────────────────────────────────────────────────────────────
    // POST /vendor/create (async fire-and-forget)
    // ─────────────────────────────────────────────────────────────

    /**
     * Creates a vendor record and triggers background AI processing.
     * Returns immediately — AI analysis runs asynchronously.
     *
     * Example request:
     *   POST /vendor/create
     *   { "input": "Vendor: Acme Corp. NDA terms: 3-year confidentiality..." }
     *
     * @param request validated NDA input from client
     * @return 202 Accepted immediately; AI processing continues in background
     */
    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody NdaAnalysisRequest request) {
        System.out.println("[VendorController] POST /vendor/create received (async mode)");
        vendorService.processRecommendationsAsync(request.getInput());
        return ResponseEntity.accepted().body("Vendor created. AI recommendations are being generated in the background.");
    }
}
