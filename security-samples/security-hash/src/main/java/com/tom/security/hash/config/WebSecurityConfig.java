package com.tom.security.hash.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfigurationSource;

import com.tom.security.hash.exception.security.AuthEntryPointJwt;
import com.tom.security.hash.logic.security.JwtAuthenticationFilter;
import com.tom.security.hash.logic.security.LogoutUtils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	@Value("${security.front.csrf:false}")
	private boolean enableCsrf;

	@Value("${security.https-only:false}")
	private boolean allowOnlyHTTPS;

	private final CorsConfigurationSource corsConfigurationSource;
	private final AuthEntryPointJwt unauthorizedHandler;
	private final JwtAuthenticationFilter jwtFilter;
	private final LogoutUtils logoutUtils;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		if (enableCsrf) {
			http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
		} else {
			http.csrf(AbstractHttpConfigurer::disable);
		}

		if (allowOnlyHTTPS) {
			http.headers(headers -> headers.httpStrictTransportSecurity(
					hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000)));
		}

		http.headers(headers -> headers
					.contentSecurityPolicy(csp -> csp.policyDirectives(
							"default-src 'self'; " + 
							"script-src 'self'; " + 
							"object-src 'none'; " + 
							"frame-ancestors 'self';"))
					.frameOptions(frame -> frame.sameOrigin()))
				.cors(cors -> cors.configurationSource(corsConfigurationSource))

				.exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

				.logout(logout -> logout
					.logoutUrl("/v1/auth/logout")
					.addLogoutHandler(logoutUtils)
					.logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
				)
				
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/v1/auth/sign-up", "/v1/auth/sign-in", "/v1/auth/refresh-token").permitAll()
						.requestMatchers("/v1/auth/logout").permitAll() 
					    .requestMatchers("/actuator/**", "/error", "/config").permitAll()
						.anyRequest().authenticated());

		return http.build();
	}

}

/*
 * Use when you don't wanna refer the website link you are sending it.
 * .referrerPolicy(referrer ->
 * referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
 */
