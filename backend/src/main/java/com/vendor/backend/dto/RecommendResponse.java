package com.vendor.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response DTO returned by the /vendor/recommend endpoint.
 *
 * Mirrors the Flask AI /recommend endpoint response:
 * {
 *   "recommendations": [
 *     { "action_type": "Review", "description": "..." },
 *     ...
 *   ],
 *   "is_fallback": false,
 *   "generated_at": "..."
 * }
 */
public class RecommendResponse {

    private List<Recommendation> recommendations;

    @JsonProperty("is_fallback")
    private boolean isFallback;

    @JsonProperty("generated_at")
    private String generatedAt;

    // Default constructor
    public RecommendResponse() {}

    public List<Recommendation> getRecommendations() { return recommendations; }
    public void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }

    public boolean isIsFallback() { return isFallback; }
    public void setIsFallback(boolean isFallback) { this.isFallback = isFallback; }

    public String getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }

    /**
     * Nested class representing a single recommendation item.
     */
    public static class Recommendation {

        @JsonProperty("action_type")
        private String actionType;

        private String description;

        public Recommendation() {}

        public Recommendation(String actionType, String description) {
            this.actionType = actionType;
            this.description = description;
        }

        public String getActionType() { return actionType; }
        public void setActionType(String actionType) { this.actionType = actionType; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
