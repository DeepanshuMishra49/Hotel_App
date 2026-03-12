package com.example.Project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "property_inquiries")
public class PropertyInquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id")
	private Property property;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	private String phone;

	@Column(length = 2000)
	private String message;

	private LocalDateTime preferredVisitTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private InquiryStatus status = InquiryStatus.NEW;

	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getPreferredVisitTime() {
		return preferredVisitTime;
	}

	public void setPreferredVisitTime(LocalDateTime preferredVisitTime) {
		this.preferredVisitTime = preferredVisitTime;
	}

	public InquiryStatus getStatus() {
		return status;
	}

	public void setStatus(InquiryStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

