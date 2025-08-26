package com.tom.auth.monolithic.controller;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.auth.monolithic.user.dto.admin.AdminPasswordUpdateRequest;
import com.tom.auth.monolithic.user.dto.admin.UserAdminResponse;
import com.tom.auth.monolithic.user.dto.admin.DeleteListResponse;
import com.tom.auth.monolithic.user.dto.admin.DeleteUsersRequest;
import com.tom.auth.monolithic.user.dto.admin.PageAdminLoginHistoryResponse;
import com.tom.auth.monolithic.user.dto.admin.PageAdminUserResponse;
import com.tom.auth.monolithic.user.dto.admin.PageLoginHistoryResponse;
import com.tom.auth.monolithic.user.dto.authentication.PasswordAuthenticationRequest;
import com.tom.auth.monolithic.user.dto.user.PasswordUpdateRequest;
import com.tom.auth.monolithic.user.dto.user.UpdateAccountRequest;
import com.tom.auth.monolithic.user.service.AdminService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Profile("prod")
@Validated
@RestController
@RequestMapping("/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService service;
	
	@GetMapping(value = "/me", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserAdminResponse> getCurrentAdminUser() {
		var response = service.getCurrentUserAdmin();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/search",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageAdminUserResponse> searchUserByParamsAdmin(
			@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) @Min(1) Integer age,
			@RequestParam(required = false) String roles,
			@RequestParam(required = false) Boolean accountLocked
			) {
		var response = service.searchUserByParamsAdmin(page, username, email, age, roles, accountLocked);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/search/id",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserAdminResponse> searchUserById(
			@RequestParam UUID userId
			) {
		var response = service.searchUserById(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/search/last-login", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageAdminLoginHistoryResponse> seeTheLastUserConnected(
			@RequestParam(defaultValue="0") @Min(0) int page) {
		var response = service.seelastUserConnected(page);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/search/history", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageLoginHistoryResponse> viewAnUserLoginHistory(
			@RequestParam(defaultValue="0") @Min(0) int page, 
			@RequestParam(required = false) String identifier) {
		var response = service.viewAnUserLoginHistory(page, identifier);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping(value = "/password/change/{user}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> changeAnUserPassword(
			@PathVariable("user") String identifier,
			@RequestBody AdminPasswordUpdateRequest request) {
		service.changeAnUserPassword(identifier, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping(value = "/password/change/admin", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> changeAdminPassword(
			@RequestBody PasswordUpdateRequest request) {
		service.changeAdminPassword(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping(value = "/update/{user}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserAdminResponse> updateAnUser(
			@PathVariable("user") String identifier,
			@RequestBody UpdateAccountRequest request
			) {
		var response = service.updateAnUser(identifier, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
	
	@PutMapping(value = "/update/admin")
	public ResponseEntity<UserAdminResponse> updateUserAdmin(
			@RequestBody UpdateAccountRequest request
			) {
		var response = service.updateUserAdmin(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
	
	@PostMapping(value = "/logout/everyone", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> logoutEveryone(
			@RequestBody PasswordAuthenticationRequest request) {
		service.logoutEveryone(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PostMapping(value = "/ban/{user}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserAdminResponse> banUser(
			@PathVariable("user") String identifier
			) {
		var response = service.banUser(identifier);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}
	
	@PostMapping(value = "/unban/{user}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserAdminResponse> unbanUser(
			@PathVariable("user") String identifier
			) {
		var response = service.unbanUser(identifier);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping(value = "/delete/{user}")
	public ResponseEntity<Void> deleteAnUser(
			@PathVariable("user") String identifier
			) {
		service.deleteAnUser(identifier);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping(value = "/delete/list", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DeleteListResponse> deleteUsers(
			@RequestBody DeleteUsersRequest request
			) {
		var response = service.deleteUsers(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
