package com.example.Project.repository;

import com.example.Project.model.InquiryStatus;
import com.example.Project.model.Property;
import com.example.Project.model.PropertyInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyInquiryRepository extends JpaRepository<PropertyInquiry, Long> {

	List<PropertyInquiry> findByProperty(Property property);

	List<PropertyInquiry> findByStatus(InquiryStatus status);
}

