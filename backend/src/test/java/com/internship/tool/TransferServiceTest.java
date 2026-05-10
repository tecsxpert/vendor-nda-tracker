package com.internship.tool;

import com.internship.tool.dto.DescribeResponse;
import com.internship.tool.dto.RecommendResponse;
import com.internship.tool.dto.ReportResponse;
import com.internship.tool.dto.TransferRequest;
import com.internship.tool.service.AiServiceClient;
import com.internship.tool.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TransferService.
 *
 * Uses Mockito to mock AiServiceClient — tests run without the Flask service.
 * Covers: successful AI responses, fallback when AI is null, blank input handling.
 */
class TransferServiceTest {

    private AiServiceClient mockAiClient;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        mockAiClient = Mockito.mock(AiServiceClient.class);
        transferService = new TransferService(mockAiClient);
    }

    // ─── describeTransfer ───────────────────────────────────────

    @Test
    void describeTransfer_whenAiReturnsResult_shouldReturnAiResponse() {
        DescribeResponse aiResponse = new DescribeResponse(
                "Transferring EU customer PII to US under SCCs.", false, "2026-05-08T10:00:00Z");
        when(mockAiClient.callDescribeAPI(anyString())).thenReturn(aiResponse);

        TransferRequest request = new TransferRequest("EU to US data transfer under Standard Contractual Clauses.");
        DescribeResponse result = transferService.describeTransfer(request);

        assertNotNull(result);
        assertFalse(result.isIsFallback());
        assertEquals("Transferring EU customer PII to US under SCCs.", result.getResult());
    }

    @Test
    void describeTransfer_whenAiReturnsNull_shouldReturnFallback() {
        when(mockAiClient.callDescribeAPI(anyString())).thenReturn(null);
        TransferRequest request = new TransferRequest("Some valid cross-border transfer details here");

        DescribeResponse result = transferService.describeTransfer(request);

        assertNotNull(result);
        assertTrue(result.isIsFallback());
        assertNotNull(result.getResult());
    }

    @Test
    void describeTransfer_whenInputBlank_shouldReturnFallback() {
        TransferRequest request = new TransferRequest("   ");
        // sanitize strips to empty — service short-circuits before calling AI
        // We need to bypass @NotBlank since we're calling service directly
        DescribeResponse result = transferService.describeTransfer(request);
        assertNotNull(result);
        assertTrue(result.isIsFallback());
    }

    // ─── getRecommendations ──────────────────────────────────────

    @Test
    void getRecommendations_whenAiReturnsResult_shouldReturnRecommendations() {
        RecommendResponse aiResponse = new RecommendResponse();
        aiResponse.setIsFallback(false);
        aiResponse.setRecommendations(List.of(
                new RecommendResponse.Recommendation("Review", "Verify SCC clauses are up-to-date"),
                new RecommendResponse.Recommendation("Encrypt", "Ensure data is encrypted in transit")
        ));
        when(mockAiClient.callRecommendAPI(anyString())).thenReturn(aiResponse);

        TransferRequest request = new TransferRequest("Transfer of financial data from UK to India without encryption.");
        RecommendResponse result = transferService.getRecommendations(request);

        assertNotNull(result);
        assertFalse(result.isIsFallback());
        assertEquals(2, result.getRecommendations().size());
        assertEquals("Review", result.getRecommendations().get(0).getActionType());
    }

    @Test
    void getRecommendations_whenAiReturnsNull_shouldReturnFallback() {
        when(mockAiClient.callRecommendAPI(anyString())).thenReturn(null);
        TransferRequest request = new TransferRequest("Some valid transfer data for testing purposes here");

        RecommendResponse result = transferService.getRecommendations(request);

        assertNotNull(result);
        assertTrue(result.isIsFallback());
        assertNotNull(result.getRecommendations());
        assertFalse(result.getRecommendations().isEmpty());
    }

    // ─── generateReport ──────────────────────────────────────────

    @Test
    void generateReport_whenAiReturnsResult_shouldReturnReport() {
        ReportResponse aiResponse = new ReportResponse();
        aiResponse.setIsFallback(false);
        aiResponse.setCached(false);
        ReportResponse.Report report = new ReportResponse.Report();
        report.setTitle("Cross-Border Transfer Risk Report");
        report.setSummary("High risk due to inadequate legal basis.");
        report.setKeyItems(List.of("No valid SCC", "No DPA in place"));
        report.setRecommendations(List.of("Sign DPA", "Update SCCs to 2021 version"));
        report.setRiskLevel("HIGH");
        aiResponse.setReport(report);
        when(mockAiClient.callGenerateReportAPI(anyString())).thenReturn(aiResponse);

        TransferRequest request = new TransferRequest("Transfer of health data from EU to US without valid legal basis.");
        ReportResponse result = transferService.generateReport(request);

        assertNotNull(result);
        assertFalse(result.isIsFallback());
        assertNotNull(result.getReport());
        assertEquals("Cross-Border Transfer Risk Report", result.getReport().getTitle());
        assertEquals("HIGH", result.getReport().getRiskLevel());
    }

    @Test
    void generateReport_whenAiReturnsNull_shouldReturnFallbackReport() {
        when(mockAiClient.callGenerateReportAPI(anyString())).thenReturn(null);
        TransferRequest request = new TransferRequest("Any valid transfer input text for this test case here");

        ReportResponse result = transferService.generateReport(request);

        assertNotNull(result);
        assertTrue(result.isIsFallback());
        assertNotNull(result.getReport());
        assertEquals("Risk Report Unavailable", result.getReport().getTitle());
    }

    @Test
    void generateReport_fallback_shouldHaveNonEmptyItems() {
        when(mockAiClient.callGenerateReportAPI(anyString())).thenReturn(null);
        TransferRequest request = new TransferRequest("Transfer text that is long enough to pass validation");

        ReportResponse result = transferService.generateReport(request);

        assertNotNull(result.getReport().getKeyItems());
        assertFalse(result.getReport().getKeyItems().isEmpty());
        assertNotNull(result.getReport().getRecommendations());
        assertFalse(result.getReport().getRecommendations().isEmpty());
    }
}
