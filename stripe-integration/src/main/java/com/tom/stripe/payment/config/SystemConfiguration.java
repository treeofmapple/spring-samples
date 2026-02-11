package com.tom.stripe.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
// @EnableJpaAuditing(auditorAwareRef = "auditorAware", dateTimeProviderRef = "dateTimeProvider")
public class SystemConfiguration {

	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}
	
	/*
	
	private final UserRepository repository;
	
	@Bean
	UserDetailsService userDetailsService() {
		return identifier -> {
			return repository.findByEmail(identifier)
					.orElseThrow(() -> new NotFoundException("User not found with identifier: " + identifier));
		};
	}

	@Bean
	AuditorAware<String> auditorAware() {
		return new ApplicationAuditAware();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new Argon2PasswordEncoder(
				16, // saltLength
				32, // hashLength
				1, // parallelism
				16384, // 64MB memory
				2 // iterations
		);
	}
	// return new BCryptPasswordEncoder(12);
	
	*/
}
