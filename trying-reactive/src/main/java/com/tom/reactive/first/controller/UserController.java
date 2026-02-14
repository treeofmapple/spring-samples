package com.tom.reactive.first.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.reactive.first.dto.PageUserResponse;
import com.tom.reactive.first.dto.UserRequest;
import com.tom.reactive.first.dto.UserResponse;
import com.tom.reactive.first.dto.UserUpdateRequest;
import com.tom.reactive.first.service.UserService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<UserResponse> searchUserById(@PathVariable(value = "id") UUID userId) {
		return service.searchUserById(userId);
	}

	@GetMapping(value = "/search")
	@ResponseStatus(HttpStatus.OK)
	public Mono<PageUserResponse> searchUserByParams(
			@RequestParam(defaultValue = "0", required = false) @Min(0) int page,
			@RequestParam(required = false) String nickname, @RequestParam(required = false) String email,
			@RequestParam(required = false) String role) {
		return service.searchUserByParams(page, nickname, email, role);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<UserResponse> createMail(@RequestBody UserRequest request) {
		return service.createUser(request);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<UserResponse> updateUserInfo(@RequestBody UserUpdateRequest request) {
		return service.updateUserInfo(request);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteUser(@PathVariable(value = "id") UUID userId) {
		return service.deleteUser(userId);
	}

}
