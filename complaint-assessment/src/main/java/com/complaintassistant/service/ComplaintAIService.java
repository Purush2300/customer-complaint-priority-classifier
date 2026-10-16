
package com.complaintassistant.service;

import com.complaintassistant.Entity.Complaint;
import com.complaintassistant.model.ComplaintAnalysis;
import com.complaintassistant.repository.ComplaintRepository;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintAIService {

    private final ChatClient chatClient;
    private final ComplaintRepository complaintRepository;

    public ComplaintAIService(
            ChatClient.Builder chatClientBuilder,
            ComplaintRepository complaintRepository) {

        this.chatClient = chatClientBuilder.build();
        this.complaintRepository = complaintRepository;
    }

    @Transactional
    public Complaint analyzeAndSaveComplaint(String description) {

        // 1. Ask Gemini to analyze the complaint
        ComplaintAnalysis analysis = chatClient.prompt()
                .system("""
                        You are a customer complaint priority classifier.

                        Analyze the customer's complaint and return:
                        summary, priority, reason, suggestedAction.

                        Choose exactly one priority:
                        LOW, MEDIUM, HIGH, or URGENT.

                        Guidelines:
                        LOW: Minor inconvenience or general feedback.
                        MEDIUM: Routine issue requiring investigation.
                        HIGH: Significant financial loss or inability
                        to access a paid service.
                        URGENT: Serious safety risk, suspected account
                        takeover, or major ongoing service disruption.

                        Do not invent facts.
                        """)
                .user("Analyze this complaint: " + description)
                .call()
                .entity(ComplaintAnalysis.class);

        // 2. Validate the priority returned by Gemini
        
        if (analysis == null
                || analysis.getPriority() == null
                || !java.util.Set.of("LOW", "MEDIUM", "HIGH", "URGENT")
                        .contains(analysis.getPriority().trim().toUpperCase())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "AI returned an invalid complaint analysis");
        }
        // 3. Create a Complaint entity
        Complaint complaint = new Complaint();
        complaint.setDescription(description);
        complaint.setSummary(analysis.getSummary());
        complaint.setPriority(
                analysis.getPriority().trim().toUpperCase());
        complaint.setReason(analysis.getReason());
        complaint.setSuggestedAction(analysis.getSuggestedAction());

        // 4. Save the complaint in MySQL
        return complaintRepository.save(complaint);
    }
}
