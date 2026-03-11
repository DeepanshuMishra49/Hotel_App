package com.example.Project.controller;

import com.example.Project.dto.PropertyRequest;
import com.example.Project.model.Property;
import com.example.Project.model.PropertyStatus;
import com.example.Project.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

	private final PropertyService propertyService;

	public PropertyController(PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	@GetMapping
	public ResponseEntity<List<Property>> getAll(
			@RequestParam Optional<String> city,
			@RequestParam Optional<String> listingType,
			@RequestParam Optional<BigDecimal> minPrice,
			@RequestParam Optional<BigDecimal> maxPrice
	) {
		List<Property> properties = propertyService.findAll(city, listingType, minPrice, maxPrice);
		return ResponseEntity.ok(properties);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Property> getById(@PathVariable Long id) {
		return propertyService.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Property> create(@Valid @RequestBody PropertyRequest request) {
		Property created = propertyService.create(request);
		return ResponseEntity.created(URI.create("/api/properties/" + created.getId()))
				.body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Property> update(@PathVariable Long id,
										   @Valid @RequestBody PropertyRequest request) {
		return propertyService.update(id, request)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Property> updateStatus(@PathVariable Long id,
												 @RequestParam PropertyStatus status) {
		return propertyService.markStatus(id, status)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		boolean removed = propertyService.delete(id);
		return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleIllegalArgument(Exception ex) {
		return ex.getMessage();
	}
}

