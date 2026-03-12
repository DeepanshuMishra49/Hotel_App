package com.example.Project.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PropertyInquiryRequest {

	@NotBlank
	private String name;

	@NotBlank
	@Email
	private String email;

	private String phone;

	@NotBlank
	private String message;

	private LocalDateTime preferredVisitTime;

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
}

