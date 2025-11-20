package com.tom.front.full.controller;

import org.springframework.http.HttpStatus;
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

import com.tom.front.full.dto.EmployeeRequest;
import com.tom.front.full.dto.EmployeeResponse;
import com.tom.front.full.dto.EmployeeUpdate;
import com.tom.front.full.dto.PageEmployeeResponse;
import com.tom.front.full.service.EmployeeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService service;
	
	@GetMapping(value = "/search")
	public ResponseEntity<PageEmployeeResponse> searchEmployeeByParams(
			@RequestParam(defaultValue = "0") @Min(0) int page,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String jobTitle,
			@RequestParam(required = false) String employeeCode
			) {
		var response = service.searchByParams(page, name, email, jobTitle, employeeCode);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/page")
	public ResponseEntity<PageEmployeeResponse> findEmployeesByPage(
			@RequestParam(defaultValue = "0") @Min(0) int page
			) {
		var response = service.findEmployeesByPage(page);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<EmployeeResponse> findEmployeeById(
			@PathVariable("id") long query
			) {
		var response = service.findById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "/create")
	public ResponseEntity<EmployeeResponse> createEmployee(
			@RequestBody @Valid EmployeeRequest request
			) {
		var response = service.createEmployee(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "/generate/{id}")
	public ResponseEntity<EmployeeResponse> generateNewEmployeeCode(
			@PathVariable(value = "id", required = true) long query
			) {
		var response = service.generateNewEmployeeCode(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping(value = "/update/{id}")
	public ResponseEntity<EmployeeResponse> updateEmployee(
			@PathVariable(value = "id", required = true) long query,
			@RequestBody @Valid EmployeeUpdate request
			) {
		var response = service.updateEmployee(query, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<Void> deleteEmployeeById(
			@PathVariable(value = "id", required = true) long query
			) {
		service.deleteById(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
