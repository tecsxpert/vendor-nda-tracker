
package com.vendor.backend.controller;

import com.vendor.backend.service.AiServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private AiServiceClient aiServiceClient;

    @GetMapping("/describe")
    public String describe(@RequestParam String text) {
        return aiServiceClient.describe(text);
    }
}