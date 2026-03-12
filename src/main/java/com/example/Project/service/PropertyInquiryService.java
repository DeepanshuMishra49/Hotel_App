package com.example.Project.service;

import com.example.Project.dto.PropertyInquiryRequest;
import com.example.Project.model.InquiryStatus;
import com.example.Project.model.Property;
import com.example.Project.model.PropertyInquiry;
import com.example.Project.repository.PropertyInquiryRepository;
import com.example.Project.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropertyInquiryService {

	private final PropertyInquiryRepository inquiryRepository;
	private final PropertyRepository propertyRepository;

	public PropertyInquiryService(PropertyInquiryRepository inquiryRepository,
								  PropertyRepository propertyRepository) {
		this.inquiryRepository = inquiryRepository;
		this.propertyRepository = propertyRepository;
	}

	public Optional<PropertyInquiry> create(Long propertyId, PropertyInquiryRequest request) {
		return propertyRepository.findById(propertyId)
				.map(property -> {
					PropertyInquiry inquiry = new PropertyInquiry();
					inquiry.setProperty(property);
					inquiry.setName(request.getName());
					inquiry.setEmail(request.getEmail());
					inquiry.setPhone(request.getPhone());
					inquiry.setMessage(request.getMessage());
					inquiry.setPreferredVisitTime(request.getPreferredVisitTime());
					inquiry.setStatus(InquiryStatus.NEW);
					return inquiryRepository.save(inquiry);
				});
	}

	public List<PropertyInquiry> findByProperty(Long propertyId) {
		Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
		if (propertyOpt.isEmpty()) {
			return List.of();
		}
		return inquiryRepository.findByProperty(propertyOpt.get());
	}

	public List<PropertyInquiry> findAll(Optional<InquiryStatus> status) {
		if (status.isPresent()) {
			return inquiryRepository.findByStatus(status.get());
		}
		return inquiryRepository.findAll();
	}

	public Optional<PropertyInquiry> updateStatus(Long id, InquiryStatus status) {
		return inquiryRepository.findById(id)
				.map(existing -> {
					existing.setStatus(status);
					return inquiryRepository.save(existing);
				});
	}
}

