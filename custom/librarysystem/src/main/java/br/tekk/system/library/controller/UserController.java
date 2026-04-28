package br.tekk.system.library.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.tekk.system.library.request.UserRequest;
import br.tekk.system.library.request.UserResponse;
import br.tekk.system.library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@Operation(summary = "Get all users", description = "Retrieves a list of all users.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "No users found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get")
	public ResponseEntity<List<UserResponse>> findAllUsers() {
		return ResponseEntity.ok(userService.findAllUsers());
	}

	@Operation(summary = "Get user by ID", description = "Retrieves a user by their unique ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User found"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(userService.findById(id));
	}

	@Operation(summary = "Create a new user", description = "Creates a new user and returns the user ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "User created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "406", description = "User already exists"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/create")
	public ResponseEntity<UUID> createUser(@RequestBody UserRequest request) {
		UUID userId = userService.createUser(request);
		return ResponseEntity.status(201).body(userId);
	}

	@Operation(summary = "Update a user", description = "Updates an existing user by ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "User updated successfully"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable UUID id, @RequestBody UserRequest request) {
		userService.updateUser(id, request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Delete user", description = "Deletes a user by their unique ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "User deleted successfully"),
			@ApiResponse(responseCode = "404", description = "User not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
