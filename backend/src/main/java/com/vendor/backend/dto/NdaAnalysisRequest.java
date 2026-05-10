package com.vendor.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for all AI analysis endpoints.
 *
 * Wraps the raw NDA text input that the client sends to the backend.
 * Validation annotations ensure input is not blank and within safe limits.
 */
public class NdaAnalysisRequest {

    @NotBlank(message = "Input text must not be blank")
    @Size(min = 10, max = 5000, message = "Input must be between 10 and 5000 characters")
    private String input;

    // Default constructor (required for Jackson deserialization)
    public NdaAnalysisRequest() {}

    public NdaAnalysisRequest(String input) {
        this.input = input;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "NdaAnalysisRequest{input='" + (input != null ? input.substring(0, Math.min(50, input.length())) + "..." : "null") + "'}";
    }
}
