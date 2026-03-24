package com.tom.first.simple.controller;

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

import com.tom.first.simple.dto.UserRequest;
import com.tom.first.simple.dto.UserResponse;
import com.tom.first.simple.dto.user.NameRequest;
import com.tom.first.simple.dto.user.UserGradeResponse;
import com.tom.first.simple.dto.user.UserUpdateResponse;
import com.tom.first.simple.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;
	
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UserResponse>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@GetMapping(value = "/get/name")
	public ResponseEntity<UserResponse> findByName(@RequestParam @Valid NameRequest request){
		return ResponseEntity.status(HttpStatus.OK).body(service.findByName(request));
	}
	
	@GetMapping(value = "/grade/average")
	public ResponseEntity<UserGradeResponse> averageUserGrade(@RequestParam @Valid NameRequest request){
		return ResponseEntity.status(HttpStatus.OK).body(service.averageUserGrade(request));
	}
	
	@GetMapping(value = "/grade")
	public ResponseEntity<List<UserGradeResponse>> allAverageUserGrades(){
		return ResponseEntity.status(HttpStatus.OK).body(service.allAverageUserGrades());
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
	}
	
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserUpdateResponse> updateUser(@RequestParam @Valid NameRequest nameRequest, @RequestBody @Valid UserRequest request){
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateUser(nameRequest, request));
	}
	
	@DeleteMapping(value = "/delete")
	public ResponseEntity<String> deleteUser(@RequestBody @Valid NameRequest request){
		service.deleteUser(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("Deleting user: %s", request));
	}
}
