package com.complaintassistant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.complaintassistant.Entity.Complaint;

public interface ComplaintRepository  extends JpaRepository<Complaint, Long>{

}
