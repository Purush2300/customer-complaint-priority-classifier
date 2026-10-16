package com.complaintassistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ComplaintRequest {

	 @NotBlank(message = "Complaint description is required")
	    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
	    private String description;

	    public ComplaintRequest() {
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

		
}
