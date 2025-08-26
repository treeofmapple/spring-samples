package com.tom.sample.auth.service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.sample.auth.common.EntityUpdater;
import com.tom.sample.auth.common.Operations;
import com.tom.sample.auth.common.ServiceLogger;
import com.tom.sample.auth.config.MailService;
import com.tom.sample.auth.dto.AuthenticationRequest;
import com.tom.sample.auth.dto.AuthenticationResponse;
import com.tom.sample.auth.dto.PasswordRequest;
import com.tom.sample.auth.dto.RegisterRequest;
import com.tom.sample.auth.dto.UpdateRequest;
import com.tom.sample.auth.dto.UserResponse;
import com.tom.sample.auth.exception.AlreadyExistsException;
import com.tom.sample.auth.exception.IllegalStatusException;
import com.tom.sample.auth.exception.NotFoundException;
import com.tom.sample.auth.mapper.AuthenticationMapper;
import com.tom.sample.auth.model.User;
import com.tom.sample.auth.model.enums.Role;
import com.tom.sample.auth.repository.UserRepository;
import com.tom.sample.auth.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	// details the logs

	@Value("${application.security.expiration}")
	private String jwtExpiration;

	@Value("${application.security.refresh-token.expiration}")
	private String refreshExpiration;

	private final JwtService jwtService;
	private final AuthenticationMapper mapper;
	private final AuthenticationManager authManager;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository repository;
	private final EntityUpdater updater;
	private final Operations operations;
	private final MailService mailService;
	private final Environment environment;

	public UserResponse getCurrentUser(Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		return mapper.buildUserResponse(user);
	}

	// get user by name or email
	public List<UserResponse> findUser(String userInfo, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		ServiceLogger.info("The user {}, is searching for: {}", user.getUsername(), userInfo);

		var users = repository.findByUsernameOrEmailContainingIgnoreCase(userInfo);
		if (users.isEmpty()) {
			throw new NotFoundException("No users found matching: " + userInfo);
		}
		return users.stream().map(mapper::buildUserResponse).toList();
	}

	// edit connected user
	public void editUser(UpdateRequest request, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		if (!request.password().equals(request.confirmPassword())) {
			throw new IllegalStatusException("Wrong Password");
		}
		var data = updater.mergeData(user, request);
		repository.save(data);
		ServiceLogger.info("User {} changed their password", user.getUsername());
	}

	// logout connected user
	public void logout(Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		updater.revokeAllUserTokens(user);
		ServiceLogger.info("User {} has logged out. All valid tokens revoked.", user.getUsername());
	}

	// connected user
	public void changePassword(PasswordRequest request, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

		if (!passwordEncoder.matches(request.confirmationpassword(), user.getPassword())) {
			ServiceLogger.warn("Wrong Password");
			throw new IllegalStatusException("Wrong Password");
		}

		if (!request.newpassword().equals(request.confirmationpassword())) {
			ServiceLogger.warn("Passwords are not the same");
			throw new IllegalStatusException("Passwords are not the same");
		}

		user.setPassword(passwordEncoder.encode(request.newpassword()));
		repository.save(user);
		ServiceLogger.info("User {} changed their password", user.getUsername());
	}

	// Admin
	public void changePassword(String userInfo, PasswordRequest request, Principal connectedUser) {
		var admin = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

		var user = repository.findByUsername(userInfo).or(() -> repository.findByEmail(userInfo))
				.orElseThrow(() -> new NotFoundException("User not found"));

		if (!passwordEncoder.matches(request.confirmationpassword(), user.getPassword())) {
			throw new IllegalStatusException("Wrong Password");
		}

		if (!request.newpassword().equals(request.confirmationpassword())) {
			throw new IllegalStatusException("Passwords are not the same");
		}

		user.setPassword(passwordEncoder.encode(request.newpassword()));
		repository.save(user);
		ServiceLogger.info("Password changed for user {} by {}", userInfo, admin);
	}

	@Transactional
	public AuthenticationResponse register(RegisterRequest request) {
		if (repository.existsByUsername(request.username()) || repository.existsByEmail(request.email())) {
			throw new AlreadyExistsException("User already exists");
		}

		if (request.password().equals(request.confirmpassword())) {
			throw new IllegalStatusException("Passwords are not the same");
		}

		var user = mapper.buildAttributes(request.name(), request.username(), request.age(), request.email(),
				passwordEncoder.encode(request.password()), false, operations.generateVerificationToken());
		user.setRole(Role.USER);
		var savedUser = repository.save(user);
		
	    boolean sslEnabled = Boolean.parseBoolean(environment.getProperty("server.ssl.enabled", "false"));
	    String protocol = sslEnabled ? "https" : "http";
	    String serverPort = environment.getProperty("server.port", "8080");
	    String verificationToken = user.getVerificationToken();
	    String verificationLink = protocol + "://" + operations.getPublicIp() + ":" + serverPort 
	                               + "/verify-email?token=" + verificationToken;

		mailService.sendEmail(user.getEmail(), "Verify your Email", "Click on the link to verificate your account: " + verificationLink);
		
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		updater.saveUserToken(savedUser, jwtToken);

		ServiceLogger.info("User registered: {}", request.username());
		var response = mapper.buildResponse(jwtToken, refreshToken);
		return response;
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletResponse response) {
		String userIdentifier = request.userinfo();

		var user = repository.findByUsername(userIdentifier).or(() -> repository.findByEmail(userIdentifier))
				.orElseThrow(() -> new NotFoundException("Username or email wasn't found"));

	    if (!user.isEmailVerified()) {
	        throw new IllegalStatusException("Please verify your email before login");
	    }
	    
		authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), request.password()));

		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		updater.revokeAllUserTokens(user);
		updater.saveUserToken(user, jwtToken);
		ServiceLogger.info("User authenticated: {}", userIdentifier);

		operations.addCookie(response, "access_token", jwtToken, operations.parseDuration(jwtExpiration));
		operations.addCookie(response, "refresh_token", refreshToken, operations.parseDuration(refreshExpiration));
		var responses = mapper.buildResponse(jwtToken, refreshToken);
		return responses;
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userInfo;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new NotFoundException("Auth token was not found");
		}
		refreshToken = authHeader.substring(7);
		userInfo = jwtService.extractUsername(refreshToken);
		if (userInfo != null) {
			var user = repository.findByUsername(userInfo).or(() -> repository.findByEmail(userInfo))
					.orElseThrow(() -> new NotFoundException("User username or email not found"));
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				updater.revokeAllUserTokens(user);
				updater.saveUserToken(user, accessToken);
				operations.addCookie(response, "access_token", accessToken, operations.parseDuration(jwtExpiration));
				var authResponse = mapper.buildResponse(accessToken, refreshToken);
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
				ServiceLogger.info("Access token refreshed for user {}", userInfo);
			}
		}
	}

	public void verificateEmail(String token) {
	    var user = repository.findByVerificationToken(token)
	            .orElseThrow(() -> new NotFoundException("Invalid verification token"));
	    user.setEmailVerified(true);
	    user.setVerificationToken(null);
	    repository.save(user);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional
	public String deleteMe(Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		updater.revokeAllUserTokens(user);
		repository.deleteById(user.getId());
		ServiceLogger.info("User {} has deleted their account", user.getUsername());
		return "The user" + user.getUsername() + "was deleted";
	}

}
