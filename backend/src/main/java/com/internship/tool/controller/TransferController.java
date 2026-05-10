package com.internship.tool.controller;

import com.internship.tool.dto.DescribeResponse;
import com.internship.tool.dto.RecommendResponse;
import com.internship.tool.dto.ReportResponse;
import com.internship.tool.dto.TransferRequest;
import com.internship.tool.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing Cross-Border Data Transfer analysis endpoints.
 *
 * Base URL: /api
 *
 * Endpoints:
 *  POST /api/describe         → AI plain-language description of the transfer
 *  POST /api/recommend        → AI actionable compliance recommendations
 *  POST /api/generate-report  → AI full risk report
 *  POST /api/create           → async fire-and-forget transfer creation
 *
 * All endpoints accept JSON: { "input": "transfer details..." }
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    // ─────────────────────────────────────────────────────────────
    // POST /api/describe
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates a plain-language description of the cross-border data transfer.
     *
     * Example request:
     *   POST /api/describe
     *   { "input": "Transferring customer PII from EU to US under Standard Contractual Clauses." }
     *
     * @return 200 OK with DescribeResponse
     */
    @PostMapping("/describe")
    public ResponseEntity<DescribeResponse> describe(@Valid @RequestBody TransferRequest request) {
        System.out.println("[TransferController] POST /api/describe received");
        return ResponseEntity.ok(transferService.describeTransfer(request));
    }

    // ─────────────────────────────────────────────────────────────
    // POST /api/recommend
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates actionable compliance recommendations for the data transfer.
     *
     * @return 200 OK with RecommendResponse
     */
    @PostMapping("/recommend")
    public ResponseEntity<RecommendResponse> recommend(@Valid @RequestBody TransferRequest request) {
        System.out.println("[TransferController] POST /api/recommend received");
        return ResponseEntity.ok(transferService.getRecommendations(request));
    }

    // ─────────────────────────────────────────────────────────────
    // POST /api/generate-report
    // ─────────────────────────────────────────────────────────────

    /**
     * Generates a full structured risk report for the data transfer.
     *
     * @return 200 OK with ReportResponse
     */
    @PostMapping("/generate-report")
    public ResponseEntity<ReportResponse> generateReport(@Valid @RequestBody TransferRequest request) {
        System.out.println("[TransferController] POST /api/generate-report received");
        return ResponseEntity.ok(transferService.generateReport(request));
    }

    // ─────────────────────────────────────────────────────────────
    // POST /api/create (async fire-and-forget)
    // ─────────────────────────────────────────────────────────────

    /**
     * Creates a transfer record and triggers background AI processing.
     * Returns 202 Accepted immediately.
     *
     * @return 202 Accepted
     */
    @PostMapping("/create")
    public ResponseEntity<String> create(@Valid @RequestBody TransferRequest request) {
        System.out.println("[TransferController] POST /api/create received (async mode)");
        transferService.processTransferAsync(request.getInput());
        return ResponseEntity.accepted()
                .body("Transfer record created. AI analysis is running in the background.");
    }
}
