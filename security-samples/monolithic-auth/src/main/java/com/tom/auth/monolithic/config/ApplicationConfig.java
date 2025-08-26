package com.tom.auth.monolithic.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tom.auth.monolithic.auditing.ApplicationAuditAware;
import com.tom.auth.monolithic.exception.NotFoundException;
import com.tom.auth.monolithic.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final UserRepository repository;

	@Bean
	UserDetailsService userDetailsService() {
		return identifier -> {
			try {
				return repository.findById(UUID.fromString(identifier))
						.orElseThrow(() -> new NotFoundException("User not found with ID: " + identifier));
			} catch (IllegalArgumentException e) {
				return repository.findByIdentifier(identifier)
						.orElseThrow(() -> new NotFoundException("User not found with identifier: " + identifier));
			}
		};
	}
	
	@Bean
	AuditorAware<UUID> auditorAware() {
		return new ApplicationAuditAware();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		
		// return new BCryptPasswordEncoder(12);
		
		return new Argon2PasswordEncoder(
				16, // saltLength
				32, // hashLength
				1, // parallelism
				19456, // memory
				2 // iterations
		);
		
	}
	
}
