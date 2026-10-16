
package com.complaintassistant.dto;

import java.time.LocalDateTime;

public class ComplaintResponse {

    private Long id;
    private String description;
    private String summary;
    private String priority;
    private String reason;
    private String suggestedAction;
    private String status;
    private LocalDateTime createdAt;

    public ComplaintResponse(
            Long id,
            String description,
            String summary,
            String priority,
            String reason,
            String suggestedAction,
            String status,
            LocalDateTime createdAt) {

        this.id = id;
        this.description = description;
        this.summary = summary;
        this.priority = priority;
        this.reason = reason;
        this.suggestedAction = suggestedAction;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSummary() {
        return summary;
    }

    public String getPriority() {
        return priority;
    }

    public String getReason() {
        return reason;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
