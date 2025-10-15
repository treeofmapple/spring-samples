package com.tom.auth.monolithic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.auth.monolithic.user.dto.authentication.AuthenticationRequest;
import com.tom.auth.monolithic.user.dto.authentication.AuthenticationResponse;
import com.tom.auth.monolithic.user.dto.user.RegisterRequest;
import com.tom.auth.monolithic.user.service.AuthenticationService;
import com.tom.auth.monolithic.user.service.FeatureFlagService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AnonymousController {

	private final FeatureFlagService featureFlagService;
	private final AuthenticationService authService;

	@PostMapping(value = "/sign-up",
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> registerUser(
			@RequestBody RegisterRequest request,
			HttpServletResponse response) {
		if(!featureFlagService.isSignUpEnabled()) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
		}
		
		var register = authService.register(request, response);
		return ResponseEntity.status(HttpStatus.CREATED).body(register);
	}
	
	@PostMapping(value = "/sign-in",
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> authenticateUser(
			@RequestBody AuthenticationRequest request,
			HttpServletRequest httpRequest,
			HttpServletResponse response) {
        if (!featureFlagService.isSignInEnabled()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
		
		var authenticate = authService.authenticate(request, httpRequest, response);
		return ResponseEntity.status(HttpStatus.OK).body(authenticate); 
	}
    
    @PostMapping(value = "/admin/sign-in", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(
            @RequestBody AuthenticationRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        
        var response = authService.authenticateAdmin(request, httpRequest, httpResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
	
	@PostMapping(value = "/refresh-token",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> refreshToken(
			@CookieValue(name = "${application.security.cookie-name}") String refreshToken,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		var authResponse = authService.refreshToken(refreshToken, request, response);
		return ResponseEntity.status(HttpStatus.OK).body(authResponse);
	}
	
}
