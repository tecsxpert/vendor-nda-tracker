package com.internship.tool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response DTO for the /api/recommend endpoint.
 * Contains a list of actionable recommendations for the data transfer.
 */
public class RecommendResponse {

    private List<Recommendation> recommendations;

    @JsonProperty("is_fallback")
    private boolean isFallback;

    @JsonProperty("generated_at")
    private String generatedAt;

    public RecommendResponse() {}

    // ── Getters / Setters ──
    public List<Recommendation> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }

    public boolean isIsFallback() { return isFallback; }
    public void setIsFallback(boolean isFallback) { this.isFallback = isFallback; }

    public String getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }

    // ── Inner class for each recommendation ──
    public static class Recommendation {

        @JsonProperty("action_type")
        private String actionType;

        private String description;
        private String priority;

        public Recommendation() {}

        public Recommendation(String actionType, String description) {
            this.actionType = actionType;
            this.description = description;
            this.priority = "MEDIUM";
        }

        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }
}
