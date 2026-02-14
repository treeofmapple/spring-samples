package com.tom.reactive.first.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	@Value("${settings.cors.allowed-origins:http://localhost:4200}")
	private String[] allowedOrigins;

	@Value("${settings.cors.max-age:3600}")
	private Long corsTime;

	@Bean
	CorsWebFilter corsWebFilter() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOriginPatterns(Arrays.asList(allowedOrigins));
		config.setAllowCredentials(true);
		config.setMaxAge(corsTime);

		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		config.setAllowedHeaders(
				Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));

		config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Location"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsWebFilter(source);
	}

}
