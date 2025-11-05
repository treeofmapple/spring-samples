package com.tom.first.username.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.username.dto.NameRequest;
import com.tom.first.username.dto.UsernameRequest;
import com.tom.first.username.dto.UsernameResponse;
import com.tom.first.username.service.UsernameService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/username")
@RequiredArgsConstructor
public class UsernameController {

	private final UsernameService service;
	
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<UsernameResponse>> findAll(){
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsernameResponse> findById(@PathVariable("id") Long userId){
		return ResponseEntity.status(HttpStatus.OK).body(service.findById(userId));
	}
	
	@GetMapping(value = "/get/name")
	public ResponseEntity<UsernameResponse> findByName(@RequestParam @Valid NameRequest name){
		return ResponseEntity.status(HttpStatus.OK).body(service.findByName(name));
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> createUsername(@RequestBody @Valid UsernameRequest request){
		Long id = service.createUsername(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(id);
	}
	
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsernameResponse> updateUsername(@RequestParam @Valid Long id, @RequestBody @Valid UsernameRequest request){
		var response = service.updateUsername(id, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<String> deleteUsernameById(@PathVariable("id") Long userId){
		service.deleteUsernameById(userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
	}
	
	@DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteUsernameByName(@RequestBody @Valid NameRequest name){
		service.deleteUsernameByName(name);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
	}
	
}
