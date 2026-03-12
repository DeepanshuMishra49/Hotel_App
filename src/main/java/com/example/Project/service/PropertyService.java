package com.example.Project.service;

import com.example.Project.dto.PropertyRequest;
import com.example.Project.model.Property;
import com.example.Project.model.PropertyStatus;
import com.example.Project.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class PropertyService {

	private final PropertyRepository propertyRepository;

	public PropertyService(PropertyRepository propertyRepository) {
		this.propertyRepository = propertyRepository;
	}

	public Property create(PropertyRequest request) {
		Property property = new Property();
		applyRequestToEntity(request, property);
		property.setStatus(PropertyStatus.AVAILABLE);
		return propertyRepository.save(property);
	}

	public List<Property> findAll(Optional<String> city,
								  Optional<String> listingType,
								  Optional<BigDecimal> minPrice,
								  Optional<BigDecimal> maxPrice,
								  Optional<Integer> minBedrooms,
								  Optional<PropertyStatus> status,
								  Optional<String> sortBy,
								  Optional<String> sortDir,
								  Optional<Integer> page,
								  Optional<Integer> size) {
		List<Property> all = propertyRepository.findAll();

		Stream<Property> stream = all.stream()
				.filter(p -> city.map(c -> p.getCity() != null && p.getCity().equalsIgnoreCase(c)).orElse(true))
				.filter(p -> listingType.map(t -> p.getListingType() != null &&
						p.getListingType().name().equalsIgnoreCase(t)).orElse(true))
				.filter(p -> minPrice.map(min -> p.getPrice() != null && p.getPrice().compareTo(min) >= 0).orElse(true))
				.filter(p -> maxPrice.map(max -> p.getPrice() != null && p.getPrice().compareTo(max) <= 0).orElse(true))
				.filter(p -> minBedrooms.map(minBeds -> p.getBedrooms() != null && p.getBedrooms() >= minBeds).orElse(true))
				.filter(p -> status.map(st -> p.getStatus() == st).orElse(true));

		// Sorting
		String sortByValue = sortBy.orElse("createdAt");
		boolean desc = sortDir.map(s -> s.equalsIgnoreCase("desc")).orElse(true);

		Comparator<Property> comparator;
		switch (sortByValue) {
			case "price" -> comparator = Comparator.comparing(
					Property::getPrice,
					Comparator.nullsLast(Comparator.naturalOrder())
			);
			case "bedrooms" -> comparator = Comparator.comparing(
					Property::getBedrooms,
					Comparator.nullsLast(Comparator.naturalOrder())
			);
			case "city" -> comparator = Comparator.comparing(
					p -> Optional.ofNullable(p.getCity()).orElse(""),
					String.CASE_INSENSITIVE_ORDER
			);
			default -> comparator = Comparator.comparing(
					Property::getCreatedAt,
					Comparator.nullsLast(Comparator.naturalOrder())
			);
		}
		if (desc) {
			comparator = comparator.reversed();
		}

		List<Property> sorted = stream.sorted(comparator).toList();

		// Simple in-memory pagination
		int pageNumber = page.orElse(0);
		int pageSize = size.orElse(20);
		if (pageNumber < 0) {
			pageNumber = 0;
		}
		if (pageSize <= 0) {
			pageSize = 20;
		}

		int fromIndex = pageNumber * pageSize;
		if (fromIndex >= sorted.size()) {
			return Collections.emptyList();
		}
		int toIndex = Math.min(fromIndex + pageSize, sorted.size());
		return sorted.subList(fromIndex, toIndex);
	}

	public List<Property> latest(int limit) {
		if (limit <= 0) {
			limit = 10;
		}
		return propertyRepository.findAll().stream()
				.sorted(Comparator.comparing(Property::getCreatedAt,
								Comparator.nullsLast(Comparator.naturalOrder()))
						.reversed())
				.limit(limit)
				.collect(Collectors.toList());
	}

	public Map<String, Long> statsByCity() {
		return propertyRepository.findAll().stream()
				.filter(p -> p.getCity() != null && !p.getCity().isBlank())
				.collect(Collectors.groupingBy(
						p -> p.getCity().toLowerCase(),
						Collectors.counting()
				));
	}

	public Map<PropertyStatus, Long> statsByStatus() {
		return propertyRepository.findAll().stream()
				.collect(Collectors.groupingBy(Property::getStatus, Collectors.counting()));
	}

	public Optional<Property> findById(Long id) {
		return propertyRepository.findById(id);
	}

	public Optional<Property> update(Long id, PropertyRequest request) {
		return propertyRepository.findById(id)
				.map(existing -> {
					applyRequestToEntity(request, existing);
					return propertyRepository.save(existing);
				});
	}

	public boolean delete(Long id) {
		if (!propertyRepository.existsById(id)) {
			return false;
		}
		propertyRepository.deleteById(id);
		return true;
	}

	public Optional<Property> markStatus(Long id, PropertyStatus status) {
		return propertyRepository.findById(id)
				.map(existing -> {
					existing.setStatus(status);
					return propertyRepository.save(existing);
				});
	}

	private void applyRequestToEntity(PropertyRequest request, Property property) {
		property.setTitle(request.getTitle());
		property.setDescription(request.getDescription());
		property.setAddressLine1(request.getAddressLine1());
		property.setAddressLine2(request.getAddressLine2());
		property.setCity(request.getCity());
		property.setState(request.getState());
		property.setCountry(request.getCountry());
		property.setZipCode(request.getZipCode());
		property.setPrice(request.getPrice());
		property.setBedrooms(request.getBedrooms());
		property.setBathrooms(request.getBathrooms());
		property.setAreaSqFt(request.getAreaSqFt());
		property.setFurnished(request.getFurnished());
		property.setListingType(request.getListingType());
		property.setOwnerName(request.getOwnerName());
		property.setOwnerEmail(request.getOwnerEmail());
		property.setOwnerPhone(request.getOwnerPhone());
	}
}

