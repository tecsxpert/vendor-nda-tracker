package com.internship.tool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for the /api/describe endpoint.
 * Contains a plain-language AI description of the data transfer.
 */
public class DescribeResponse {

    private String result;

    @JsonProperty("is_fallback")
    private boolean isFallback;

    @JsonProperty("generated_at")
    private String generatedAt;

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
