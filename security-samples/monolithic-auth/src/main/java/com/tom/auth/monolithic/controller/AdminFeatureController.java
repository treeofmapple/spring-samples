package com.tom.auth.monolithic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.auth.monolithic.user.service.FeatureFlagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/admin/features")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFeatureController {

	private final FeatureFlagService featureFlagService;

	@PostMapping("/sign-in/disable")
	public ResponseEntity<String> disableSignIn() {
		if (!featureFlagService.isSignInEnabled()) {
			return ResponseEntity.status(HttpStatus.OK).body("Sign-in endpoint was already disabled.");
		}

		featureFlagService.setSignInEnabled(false);
		return ResponseEntity.status(HttpStatus.OK).body("Sign-in endpoint has been disabled.");
	}

	@PostMapping("/sign-in/enable")
	public ResponseEntity<String> enableSignIn() {
		if (featureFlagService.isSignInEnabled()) {
			return ResponseEntity.status(HttpStatus.OK).body("Sign-in endpoint was already enabled.");
		}

		featureFlagService.setSignInEnabled(true);
		return ResponseEntity.status(HttpStatus.OK).body("Sign-in endpoint has been enabled.");
	}

	@GetMapping("/sign-in/status")
	public ResponseEntity<String> getSignInStatus() {
		boolean isEnabled = featureFlagService.isSignInEnabled();
		return ResponseEntity.status(HttpStatus.OK)
				.body("Sign-in endpoint is currently: " + (isEnabled ? "ENABLED" : "DISABLED"));
	}

	@PostMapping("/sign-up/disable")
	public ResponseEntity<String> disableSignUp() {
		if (!featureFlagService.isSignUpEnabled()) {
			return ResponseEntity.status(HttpStatus.OK).body("Sign-up endpoint was already disabled.");
		}

		featureFlagService.setSignUpEnabled(false);
		return ResponseEntity.status(HttpStatus.OK).body("Sign-up endpoint has been disabled.");
	}

	@PostMapping("/sign-up/enable")
	public ResponseEntity<String> enableSignUp() {
		if (featureFlagService.isSignInEnabled()) {
			return ResponseEntity.status(HttpStatus.OK).body("Sign-up endpoint was already enabled.");
		}

		featureFlagService.setSignUpEnabled(true);
		return ResponseEntity.status(HttpStatus.OK).body("Sign-up endpoint has been enabled.");
	}

	@GetMapping("/sign-up/status")
	public ResponseEntity<String> getSignUnStatus() {
		boolean isEnabled = featureFlagService.isSignUpEnabled();
		return ResponseEntity.status(HttpStatus.OK)
				.body("Sign-up endpoint is currently: " + (isEnabled ? "ENABLED" : "DISABLED"));
	}

}