package com.example.Project.controller;

import com.example.Project.dto.AuthLoginRequest;
import com.example.Project.dto.AuthRegisterRequest;
import com.example.Project.dto.AuthResponse;
import com.example.Project.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(@Valid @RequestBody AuthRegisterRequest request) {
		return authService.register(request);
	}

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody AuthLoginRequest request) {
		return authService.login(request);
	}
}

