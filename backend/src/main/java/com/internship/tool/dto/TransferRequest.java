package com.internship.tool.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for cross-border data transfer AI analysis.
 * Validated before reaching the service layer.
 */
public class TransferRequest {

    @NotBlank(message = "Input text must not be blank")
    @Size(min = 10, max = 5000, message = "Input must be between 10 and 5000 characters")
    private String input;

    public TransferRequest() {}

    public TransferRequest(String input) {
        this.input = input;
    }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }
}
