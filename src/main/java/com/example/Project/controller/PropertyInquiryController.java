package com.example.Project.controller;

import com.example.Project.dto.PropertyInquiryRequest;
import com.example.Project.model.InquiryStatus;
import com.example.Project.model.PropertyInquiry;
import com.example.Project.service.PropertyInquiryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PropertyInquiryController {

	private final PropertyInquiryService inquiryService;

	public PropertyInquiryController(PropertyInquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}

	@PostMapping("/properties/{propertyId}/inquiries")
	public ResponseEntity<PropertyInquiry> createInquiry(@PathVariable Long propertyId,
														 @Valid @RequestBody PropertyInquiryRequest request) {
		return inquiryService.create(propertyId, request)
				.map(created -> ResponseEntity.created(URI.create("/api/inquiries/" + created.getId()))
						.body(created))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/properties/{propertyId}/inquiries")
	public ResponseEntity<List<PropertyInquiry>> getInquiriesForProperty(@PathVariable Long propertyId) {
		List<PropertyInquiry> inquiries = inquiryService.findByProperty(propertyId);
		return ResponseEntity.ok(inquiries);
	}

	@GetMapping("/inquiries")
	public ResponseEntity<List<PropertyInquiry>> getAllInquiries(
			@RequestParam Optional<InquiryStatus> status
	) {
		// Requires authentication (see SecurityConfig)
		return ResponseEntity.ok(inquiryService.findAll(status));
	}

	@PatchMapping("/inquiries/{id}/status")
	public ResponseEntity<PropertyInquiry> updateInquiryStatus(@PathVariable Long id,
															   @RequestParam InquiryStatus status) {
		// Requires authentication (see SecurityConfig)
		return inquiryService.updateStatus(id, status)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}

