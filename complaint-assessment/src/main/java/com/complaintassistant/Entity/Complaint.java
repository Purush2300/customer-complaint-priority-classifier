package com.complaintassistant.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Complaint description is required")
    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @NotBlank(message = "Priority is required")
    @Size(max = 20, message = "Priority cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String priority;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "suggested_action", columnDefinition = "TEXT")
    private String suggestedAction;

    
    @Column(nullable = false, length = 20)
    private String status = "OPEN";
    
    private LocalDateTime createdAt;

    public Complaint() {
    }

    @PrePersist
    public void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

}