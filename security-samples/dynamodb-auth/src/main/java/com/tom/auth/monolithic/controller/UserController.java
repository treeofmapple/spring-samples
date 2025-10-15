package com.tom.auth.monolithic.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.auth.monolithic.user.dto.admin.PageLoginHistoryResponse;
import com.tom.auth.monolithic.user.dto.user.DeleteAccountRequest;
import com.tom.auth.monolithic.user.dto.user.PageUserResponse;
import com.tom.auth.monolithic.user.dto.user.PasswordUpdateRequest;
import com.tom.auth.monolithic.user.dto.user.UpdateAccountRequest;
import com.tom.auth.monolithic.user.dto.user.UserResponse;
import com.tom.auth.monolithic.user.service.UserService;
import com.tom.auth.monolithic.user.service.utils.CookiesUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')") 
@RequiredArgsConstructor
public class UserController {

	@Value("${application.security.cookie-name}")
	private String refreshTokenCookieName;
	
	private final UserService userService;
	private final CookiesUtils cookiesUtils;
	
	@GetMapping(value = "/search", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageUserResponse> searchUserByParams(
			@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) @Min(1) Integer age) {
		var response = userService.searchUserByParams(page, username, email, age); 
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/search/id", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> searchUserById(
			@RequestParam UUID userId) {
		var response = userService.searchUserById(userId); 
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> getCurrentUser() {
		var response = userService.getCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/me/history", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PageLoginHistoryResponse> getCurrentLoginHistory(
			@RequestParam(defaultValue = "0") int page) {
		var response = userService.viewCurrentUserLoginHistory(page);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/me/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> exportUserInfo() {
		var response = userService.exportMyUserData();
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, 
						"attachment; filename=\"" + response.fileName() + "\"")
				.contentLength(response.fileSize())
				.body(response.resource());
	}
	
	@PutMapping(value = "/me/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> editUser(
			@RequestBody UpdateAccountRequest request) {
		var response = userService.updateUser(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@PutMapping(value = "/me/password",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> changeUserPassword(
			@RequestBody PasswordUpdateRequest request) {
		userService.changeUserPassword(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@DeleteMapping(value = "/me/delete",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteMyAccount(
			@RequestBody DeleteAccountRequest request,
			HttpServletResponse response) {
		userService.deleteMyAccount(request);
		
		cookiesUtils.clearCookie(response, refreshTokenCookieName);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}

