package com.vendor.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO returned by the /vendor/describe endpoint.
 *
 * Mirrors the JSON structure from the Flask AI /describe endpoint:
 * {
 *   "result": "...",
 *   "is_fallback": false,
 *   "generated_at": "..."
 * }
 */
public class DescribeResponse {

    private String result;

    @JsonProperty("is_fallback")
    private boolean isFallback;

    @JsonProperty("generated_at")
    private String generatedAt;

    // Default constructor
    public DescribeResponse() {}

    public DescribeResponse(String result, boolean isFallback, String generatedAt) {
        this.result = result;
        this.isFallback = isFallback;
        this.generatedAt = generatedAt;
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public boolean isIsFallback() { return isFallback; }
    public void setIsFallback(boolean isFallback) { this.isFallback = isFallback; }

    public String getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }
}
