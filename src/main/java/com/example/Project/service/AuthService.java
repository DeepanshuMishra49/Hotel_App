package com.example.Project.service;

import com.example.Project.dto.AuthLoginRequest;
import com.example.Project.dto.AuthRegisterRequest;
import com.example.Project.dto.AuthResponse;
import com.example.Project.model.AppUser;
import com.example.Project.repository.UserRepository;
import com.example.Project.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository,
					   PasswordEncoder passwordEncoder,
					   AuthenticationManager authenticationManager,
					   JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public AuthResponse register(AuthRegisterRequest request) {
		if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
			throw new IllegalArgumentException("Email already registered");
		}

		AppUser user = new AppUser();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());

		AppUser saved = userRepository.save(user);

		String token = jwtService.generateToken(
				saved.getEmail(),
				Map.of("role", saved.getRole().name(), "userId", saved.getId())
		);
		return new AuthResponse(token, saved.getId(), saved.getName(), saved.getEmail(), saved.getRole());
	}

	public AuthResponse login(AuthLoginRequest request) {
		Authentication auth = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		);

		String email = auth.getName();
		AppUser user = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		String token = jwtService.generateToken(
				user.getEmail(),
				Map.of("role", user.getRole().name(), "userId", user.getId())
		);
		return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
	}
}

