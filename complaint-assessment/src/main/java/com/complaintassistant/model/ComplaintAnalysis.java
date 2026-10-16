package com.complaintassistant.model;

public class ComplaintAnalysis {

	private String summary;
    private String priority;
    private String reason;
    private String suggestedAction;
 
    public ComplaintAnalysis() {
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
    
    
    
}
