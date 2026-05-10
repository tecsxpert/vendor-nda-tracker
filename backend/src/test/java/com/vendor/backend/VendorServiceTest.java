package com.vendor.backend;

import com.vendor.backend.dto.DescribeResponse;
import com.vendor.backend.dto.NdaAnalysisRequest;
import com.vendor.backend.dto.RecommendResponse;
import com.vendor.backend.dto.ReportResponse;
import com.vendor.backend.service.AiServiceClient;
import com.vendor.backend.service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for VendorService.
 *
 * Uses Mockito to mock AiServiceClient so tests run without the Flask service.
 * Tests cover:
 *   - Successful AI responses
 *   - Fallback responses when AI returns null
 *   - Blank input handling
 */
class VendorServiceTest {

    private AiServiceClient mockAiServiceClient;
    private VendorService vendorService;

    @BeforeEach
    void setUp() {
        mockAiServiceClient = Mockito.mock(AiServiceClient.class);
        vendorService = new VendorService(mockAiServiceClient);
    }

    // ─────────────────────────────────────────────────────────────
    // Tests for describeNda()
    // ─────────────────────────────────────────────────────────────

    @Test
    void describeNda_whenAiReturnsResult_shouldReturnAiResponse() {
        // Arrange
        DescribeResponse aiResponse = new DescribeResponse(
                "This NDA restricts sharing of confidential data.", false, "2026-05-01T10:00:00Z");
        when(mockAiServiceClient.callDescribeAPI(anyString())).thenReturn(aiResponse);

        NdaAnalysisRequest request = new NdaAnalysisRequest("This NDA restricts sharing of confidential data for 2 years.");

        // Act
        DescribeResponse result = vendorService.describeNda(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.isIsFallback());
        assertEquals("This NDA restricts sharing of confidential data.", result.getResult());
    }

    @Test
    void describeNda_whenAiReturnsNull_shouldReturnFallback() {
        // Arrange
        when(mockAiServiceClient.callDescribeAPI(anyString())).thenReturn(null);
        NdaAnalysisRequest request = new NdaAnalysisRequest("Some NDA text that is valid and long enough");

        // Act
        DescribeResponse result = vendorService.describeNda(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isIsFallback());
        assertNotNull(result.getResult());
    }

    // ─────────────────────────────────────────────────────────────
    // Tests for getRecommendations()
    // ─────────────────────────────────────────────────────────────

    @Test
    void getRecommendations_whenAiReturnsResult_shouldReturnRecommendations() {
        // Arrange
        RecommendResponse aiResponse = new RecommendResponse();
        aiResponse.setIsFallback(false);
        aiResponse.setRecommendations(List.of(
                new RecommendResponse.Recommendation("Review", "Review liability clause"),
                new RecommendResponse.Recommendation("Negotiate", "Request exit clause")
        ));
        when(mockAiServiceClient.callRecommendAPI(anyString())).thenReturn(aiResponse);

        NdaAnalysisRequest request = new NdaAnalysisRequest("NDA with unlimited liability clause and no exit terms.");

        // Act
        RecommendResponse result = vendorService.getRecommendations(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.isIsFallback());
        assertEquals(2, result.getRecommendations().size());
        assertEquals("Review", result.getRecommendations().get(0).getActionType());
    }

    @Test
    void getRecommendations_whenAiReturnsNull_shouldReturnFallback() {
        // Arrange
        when(mockAiServiceClient.callRecommendAPI(anyString())).thenReturn(null);
        NdaAnalysisRequest request = new NdaAnalysisRequest("Some valid NDA text for testing purposes here");

        // Act
        RecommendResponse result = vendorService.getRecommendations(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isIsFallback());
        assertNotNull(result.getRecommendations());
        assertFalse(result.getRecommendations().isEmpty());
    }

    // ─────────────────────────────────────────────────────────────
    // Tests for generateReport()
    // ─────────────────────────────────────────────────────────────

    @Test
    void generateReport_whenAiReturnsResult_shouldReturnReport() {
        // Arrange
        ReportResponse aiResponse = new ReportResponse();
        aiResponse.setIsFallback(false);
        aiResponse.setCached(false);
        ReportResponse.Report report = new ReportResponse.Report();
        report.setTitle("Vendor NDA Risk Report");
        report.setSummary("Moderate risk due to unlimited liability.");
        report.setKeyItems(List.of("3 year term", "Unlimited liability"));
        report.setRecommendations(List.of("Negotiate liability cap"));
        aiResponse.setReport(report);

        when(mockAiServiceClient.callGenerateReportAPI(anyString())).thenReturn(aiResponse);
        NdaAnalysisRequest request = new NdaAnalysisRequest("3 year NDA with unlimited liability and strict confidentiality.");

        // Act
        ReportResponse result = vendorService.generateReport(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.isIsFallback());
        assertNotNull(result.getReport());
        assertEquals("Vendor NDA Risk Report", result.getReport().getTitle());
    }

    @Test
    void generateReport_whenAiReturnsNull_shouldReturnFallbackReport() {
        // Arrange
        when(mockAiServiceClient.callGenerateReportAPI(anyString())).thenReturn(null);
        NdaAnalysisRequest request = new NdaAnalysisRequest("Any valid NDA input text for test case here");

        // Act
        ReportResponse result = vendorService.generateReport(request);

        // Assert
        assertNotNull(result);
        assertTrue(result.isIsFallback());
        assertNotNull(result.getReport());
        assertEquals("NDA Report Unavailable", result.getReport().getTitle());
    }
}
