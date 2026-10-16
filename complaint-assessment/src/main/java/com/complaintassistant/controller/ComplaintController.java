
package com.complaintassistant.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;


import com.complaintassistant.service.ComplaintAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.complaintassistant.Entity.Complaint;
import com.complaintassistant.dto.ComplaintMapper;
import com.complaintassistant.dto.ComplaintRequest;
import com.complaintassistant.dto.ComplaintResponse;
import com.complaintassistant.service.ComplaintService;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

	private final ComplaintAIService complaintAIService;
    private final ComplaintService complaintService;
    private final ComplaintMapper complaintMapper;

    public ComplaintController(
            ComplaintService complaintService,
            ComplaintMapper complaintMapper,ComplaintAIService complaintAIService) {
        this.complaintService = complaintService;
        this.complaintMapper = complaintMapper;
        this.complaintAIService = complaintAIService;
    }

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(
            @Valid @RequestBody ComplaintRequest request) {

        Complaint complaint = new Complaint();
        complaint.setDescription(request.getDescription());
        complaint.setPriority("MEDIUM");
        complaint.setSummary("Pending analysis");
        complaint.setReason("Complaint has not been analyzed yet.");
        complaint.setSuggestedAction("Analyze the complaint.");
        complaint.setStatus("OPEN");

        
        Complaint savedComplaint =
                complaintService.createComplaint(complaint);
        ComplaintResponse response =
                complaintMapper.toResponse(savedComplaint);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAllComplaints()
                .stream()
                .map(complaintMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ComplaintResponse getComplaintById(@PathVariable Long id) {
        Complaint complaint = complaintService.getComplaintById(id);
        return complaintMapper.toResponse(complaint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }


@PutMapping("/{id}")
public ResponseEntity<ComplaintResponse> updateComplaint(
        @PathVariable Long id,
        @Valid @RequestBody ComplaintRequest request) {

    Complaint updatedComplaint = new Complaint();
    updatedComplaint.setDescription(request.getDescription());

    // Preserve fields that the request does not provide.
    // The service will update the description only.
    Complaint existingComplaint = complaintService.getComplaintById(id);

    existingComplaint.setDescription(updatedComplaint.getDescription());

    Complaint savedComplaint =
            complaintService.updateComplaint(id, existingComplaint);

    ComplaintResponse response =
            complaintMapper.toResponse(savedComplaint);

    return ResponseEntity.ok(response);
}

@PostMapping("/analyze")
public ResponseEntity<ComplaintResponse> analyzeComplaint(
        @Valid @RequestBody ComplaintRequest request) {

    Complaint analyzedComplaint =
            complaintAIService.analyzeAndSaveComplaint(
                    request.getDescription());

    ComplaintResponse response =
            complaintMapper.toResponse(analyzedComplaint);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
}



}
