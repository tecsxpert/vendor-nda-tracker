package com.internship.tool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response DTO for the /api/generate-report endpoint.
 * Contains a full structured risk report for the cross-border data transfer.
 */
public class ReportResponse {

    private Report report;

    @JsonProperty("is_fallback")
    private boolean isFallback;

    private boolean cached;

    @JsonProperty("generated_at")
    private String generatedAt;

    public ReportResponse() {}

    // ── Getters / Setters ──
    public Report getReport() { return report; }
    public void setReport(Report report) { this.report = report; }

    public boolean isIsFallback() { return isFallback; }
    public void setIsFallback(boolean isFallback) { this.isFallback = isFallback; }

    public boolean isCached() { return cached; }
    public void setCached(boolean cached) { this.cached = cached; }

    public String getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }

    // ── Inner class for the report body ──
    public static class Report {

        private String title;
        private String summary;
        private String overview;

        @JsonProperty("key_items")
        private List<String> keyItems;

        private List<String> recommendations;

        @JsonProperty("risk_level")
        private String riskLevel;

        public Report() {}

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }

        public String getOverview() { return overview; }
        public void setOverview(String overview) { this.overview = overview; }

        public List<String> getKeyItems() { return keyItems; }
        public void setKeyItems(List<String> keyItems) { this.keyItems = keyItems; }

        public List<String> getRecommendations() { return recommendations; }
        public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

        public String getRiskLevel() { return riskLevel; }
        public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    }
}
