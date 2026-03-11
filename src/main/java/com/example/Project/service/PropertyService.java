package com.example.Project.service;

import com.example.Project.dto.PropertyRequest;
import com.example.Project.model.Property;
import com.example.Project.model.PropertyStatus;
import com.example.Project.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
								  Optional<BigDecimal> maxPrice) {
		List<Property> all = propertyRepository.findAll();

		return all.stream()
				.filter(p -> city.map(c -> p.getCity() != null && p.getCity().equalsIgnoreCase(c)).orElse(true))
				.filter(p -> listingType.map(t -> p.getListingType() != null &&
						p.getListingType().name().equalsIgnoreCase(t)).orElse(true))
				.filter(p -> minPrice.map(min -> p.getPrice() != null && p.getPrice().compareTo(min) >= 0).orElse(true))
				.filter(p -> maxPrice.map(max -> p.getPrice() != null && p.getPrice().compareTo(max) <= 0).orElse(true))
				.collect(Collectors.toList());
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

