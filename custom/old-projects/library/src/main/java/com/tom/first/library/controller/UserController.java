package com.tom.first.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.library.dto.UserRequest;
import com.tom.first.library.dto.UserRequest.EmailRequest;
import com.tom.first.library.dto.UserRequest.NameRequest;
import com.tom.first.library.dto.UserRequest.PasswordRequest;
import com.tom.first.library.dto.UserResponse;
import com.tom.first.library.dto.UserResponse.UserUpdateResponse;
import com.tom.first.library.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}

	@GetMapping(value = "/get/name", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> findByUsername(@RequestParam @Valid NameRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findByUsername(request));
	}

	@GetMapping(value = "/get/email", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> findByEmail(@RequestParam @Valid EmailRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findByEmail(request));
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
	}

	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserUpdateResponse> updateUser(@RequestParam @Valid EmailRequest mail, @RequestBody @Valid UserRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.updateUser(mail, request));
	}

    @PutMapping(value = "/update/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> changePassword(@RequestParam @Valid EmailRequest mail, @RequestBody @Valid PasswordRequest request) {
		service.passwordUpdate(mail, request);
		return ResponseEntity.status(HttpStatus.OK).body(String.format("Changed password from user: %s", mail.email()));
	}

	@DeleteMapping(value = "/delete", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> deleteUserByEmail(@RequestParam @Valid EmailRequest request) {
		service.deleteUserByEmail(request);
		return ResponseEntity.status(HttpStatus.OK).body(String.format("Deleting user: %s", request.email()));
	}
}
