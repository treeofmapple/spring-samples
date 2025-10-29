package com.tom.auth.monolithic.config;

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
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfigurationSource;

import com.tom.auth.monolithic.exception.global.AuthEntryPointJwt;
import com.tom.auth.monolithic.security.CookiesUtils;
import com.tom.auth.monolithic.security.JwtAuthenticationFilter;
import com.tom.auth.monolithic.security.RateLimitFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	@Value("${application.front.csrf:false}")
	private boolean enableCsrf;

	@Value("${application.security.cookie-name}")
	private String refreshTokenCookieName;

	private final CorsConfigurationSource corsConfigurationSource;
	private final LogoutHandler logoutHandler;
	private final AuthEntryPointJwt unauthorizedHandler;
	private final RateLimitFilter rateLimit;
	private final JwtAuthenticationFilter jwtFilter;
	private final CookiesUtils cookiesUtils;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		if (enableCsrf) {
			http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
		} else {
			http.csrf(AbstractHttpConfigurer::disable);
		}

		http
		
        .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self'; " +
                        "object-src 'none'; " +
                        "frame-ancestors 'self';"
                ))
                .frameOptions(frame -> frame.sameOrigin())
            // .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true))
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource));
		
        http
            .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http
			.addFilterAfter(rateLimit, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/v1/auth/**", "/error").permitAll()
				.requestMatchers("/v1/auth/logout").hasAnyRole("USER", "ADMIN")
				.anyRequest().authenticated());
		
		http.logout(logout -> logout
				.logoutUrl("/v1/auth/logout")
				.addLogoutHandler(logoutHandler)
				.logoutSuccessHandler((request, response, authentication) -> {

					cookiesUtils.clearCookie(response, refreshTokenCookieName);

					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write("{\"message\": \"You have been logged out successfully.\"}");
				}));

		return http.build();
	}

}
