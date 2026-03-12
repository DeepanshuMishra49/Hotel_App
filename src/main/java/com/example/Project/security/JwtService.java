package com.example.Project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

	private final SecretKey key;
	private final long expirationSeconds;

	public JwtService(
			@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration-seconds:86400}") long expirationSeconds
	) {
		// Needs to be at least 256-bit for HS256 (>= 32 chars for ASCII)
		this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.expirationSeconds = expirationSeconds;
	}

	public String generateToken(String subjectEmail, Map<String, Object> extraClaims) {
		Instant now = Instant.now();
		return Jwts.builder()
				.subject(subjectEmail)
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusSeconds(expirationSeconds)))
				.claims(extraClaims)
				.signWith(key)
				.compact();
	}

	public String extractSubject(String token) {
		return parseAllClaims(token).getSubject();
	}

	public boolean isTokenValid(String token) {
		try {
			Claims claims = parseAllClaims(token);
			Date exp = claims.getExpiration();
			return exp != null && exp.after(new Date());
		} catch (Exception ex) {
			return false;
		}
	}

	private Claims parseAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}

