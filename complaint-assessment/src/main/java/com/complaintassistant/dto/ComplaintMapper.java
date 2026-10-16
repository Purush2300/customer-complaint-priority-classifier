
package com.complaintassistant.dto;

import org.springframework.stereotype.Component;

import com.complaintassistant.Entity.Complaint;

@Component
public class ComplaintMapper {

    public ComplaintResponse toResponse(Complaint complaint) {
        return new ComplaintResponse(
                complaint.getId(),
                complaint.getDescription(),
                complaint.getSummary(),
                complaint.getPriority(),
                complaint.getReason(),
                complaint.getSuggestedAction(),
                complaint.getStatus(),
                complaint.getCreatedAt()
        );
    }
}
