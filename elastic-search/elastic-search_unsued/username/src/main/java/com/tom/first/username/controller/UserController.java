package com.tom.first.username.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.username.dto.UserRequest;
import com.tom.first.username.dto.UserResponse;
import com.tom.first.username.service.UserService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/username")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@GetMapping(params = "id")
	public ResponseEntity<UserResponse> findById(@RequestParam Long userId) {
		var response = service.findById(userId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "username")
	public ResponseEntity<UserResponse> findByUsername(@RequestParam String username) {
		var response = service.findByUsername(username);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "email")
	public ResponseEntity<UserResponse> findByEmail(@RequestParam String email) {
		var response = service.findByEmail(email);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody(required = true) UserRequest request) {
		var response = service.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping(value = "/{query}")
	public ResponseEntity<Void> deleteUserById(@PathVariable long query) {
		service.deleteUserById(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
