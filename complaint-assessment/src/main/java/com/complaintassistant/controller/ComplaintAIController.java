
package com.complaintassistant.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.complaintassistant.Entity.Complaint;
import com.complaintassistant.service.ComplaintAIService;

@RestController
@RequestMapping("/api/ai")
public class ComplaintAIController {

    private final ComplaintAIService complaintAIService;

    public ComplaintAIController(
            ComplaintAIService complaintAIService) {
        this.complaintAIService = complaintAIService;
    }

    @PostMapping("/analyze-complaint")
    public Complaint analyzeComplaint(
            @RequestBody Map<String, String> request) {

        String description = request.get("description");

        if (description == null || description.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Complaint description cannot be empty");
        }

        if (description.length() > 2000) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Complaint description cannot exceed 2000 characters");
        }

        return complaintAIService.analyzeAndSaveComplaint(description);
    }
}
