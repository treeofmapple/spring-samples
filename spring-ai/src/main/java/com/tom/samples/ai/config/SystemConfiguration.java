package com.tom.samples.ai.config;

import java.time.Instant;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.web.filter.ForwardedHeaderFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableMongoAuditing(dateTimeProviderRef = "dateTimeProvider")
public class SystemConfiguration {

	@Bean
	ForwardedHeaderFilter forwardedHeaderFilter() {
		return new ForwardedHeaderFilter();
	}
	
	@Bean
    DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }
	
}
