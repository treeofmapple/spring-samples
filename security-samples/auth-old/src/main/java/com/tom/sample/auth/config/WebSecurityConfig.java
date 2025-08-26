package com.tom.sample.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import com.tom.sample.auth.common.WhitelistLoader;
import com.tom.sample.auth.exception.AuthEntryPointJwt;
import com.tom.sample.auth.security.JwtAuthenticationFilter;
import com.tom.sample.auth.security.Oauth2Authentication;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final WhitelistLoader whitelistLoader;	
	private final AuthEntryPointJwt unauthorizedHandler;
	private final JwtAuthenticationFilter filter;
	private final AuthenticationProvider provider;
	private final LogoutHandler logoutHandler;
	private final CorsConfigurationSource corsConfigurationSource;
	private final Oauth2Authentication authentication;
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	String[] whiteListUrls = whitelistLoader.loadWhitelist();
    	http
    		.headers(headers -> headers
			    .contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'"))
			    .frameOptions(frame -> frame.sameOrigin())
			)
    		.csrf(csrf -> csrf.disable())
    		.cors(cors -> cors.configurationSource(corsConfigurationSource))
    		
    		.exceptionHandling(exception -> 
    				exception.authenticationEntryPoint(unauthorizedHandler))
    		.oauth2Login(oauth2 -> oauth2
    				.loginPage("/oauth/login")
    				.successHandler(authentication)
    		)
    		.sessionManagement(session ->
    				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    				.maximumSessions(1)
    				.expiredUrl("/login?expired")
    				.maxSessionsPreventsLogin(true))
    		.authorizeHttpRequests(auth -> auth
    				.requestMatchers(whiteListUrls).permitAll()
    				//.requestMatchers("").hasAnyRole()
    				//.requestMatchers("").permitAll()
    				.anyRequest().authenticated()
    		)
    		.authenticationProvider(provider)
    		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout 
            		.logoutUrl("/api/v1/auth/logout")
        			.addLogoutHandler(logoutHandler)
        			.logoutSuccessHandler((request, response, authentication) -> 
        			SecurityContextHolder.clearContext()
                )
        );
    	
    	return http.build();
	}
	
}
