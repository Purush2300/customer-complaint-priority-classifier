package com.complaintassistant.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.complaintassistant.Entity.Complaint;
import com.complaintassistant.repository.ComplaintRepository;

@Service
public class ComplaintService {

	private final ComplaintRepository complaintRepository;
	
	
	public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public Complaint createComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }
	
    public Complaint getComplaintById(Long id) {
    	return complaintRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Complaint not found with ID: "+id));
    }
    
    
    
    public Complaint updateComplaint(Long id, Complaint updatedComplaint) {

        Complaint existingComplaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Complaint not found with ID: " + id));

        existingComplaint.setDescription(updatedComplaint.getDescription());
        existingComplaint.setPriority(updatedComplaint.getPriority());
        existingComplaint.setSummary(updatedComplaint.getSummary());
        existingComplaint.setReason(updatedComplaint.getReason());
        existingComplaint.setSuggestedAction(updatedComplaint.getSuggestedAction());
        existingComplaint.setStatus(updatedComplaint.getStatus());

        return complaintRepository.save(existingComplaint);
    }
    
    
    public void deleteComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Complaint not found with ID: " + id
                ));

        complaintRepository.delete(complaint);
    }
}
