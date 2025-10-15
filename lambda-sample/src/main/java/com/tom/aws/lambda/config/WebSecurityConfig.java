package com.tom.aws.lambda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.tom.aws.lambda.exception.global.AuthEntryPointJwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	private final AuthEntryPointJwt unauthorizedHandler;
	
	@Value("${settings.security.generated.user}")
	private String user;
	
	@Value("${settings.security.generated.password}")
	private String password;
	
	@Value("${application.endpoints.authenticated}")
	private String endpoints;
	
	@Bean
	SecurityFilterChain systemSecurityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .exceptionHandling(exception -> 
	            exception.authenticationEntryPoint(unauthorizedHandler))
	        .authorizeHttpRequests(auth -> auth
        		.requestMatchers(endpoints).authenticated()
	            .anyRequest().permitAll()
	        )
	        .httpBasic(Customizer.withDefaults()) 
	        .sessionManagement(sess -> 
	            sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .csrf(csrf -> csrf.disable());

	    return http.build();
	}
	
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails username = User.builder()
            .username(user)
            .password(passwordEncoder().encode(password))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(username);
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    
}
