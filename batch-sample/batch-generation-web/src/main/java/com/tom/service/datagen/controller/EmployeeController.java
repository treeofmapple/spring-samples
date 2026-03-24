package com.tom.service.datagen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.service.datagen.common.ConnectionUtils;
import com.tom.service.datagen.dto.EmployeeRequest;
import com.tom.service.datagen.service.EmployeeService;
import com.tom.service.datagen.service.EmployeeUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/employee")
@RequiredArgsConstructor
// @Tag(name = "GenerateEmployeeData", description = "Batch of employee Data")
public class EmployeeController {

	private final ConnectionUtils connectionUtils;
	private final EmployeeService service;
	private final EmployeeUtils utils;

	@GetMapping(value = "/{fileId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<byte[]> downloadEmployeeData(@PathVariable String fileId) {
		byte[] csvData = service.retrieveCsvFromTempStorage(fileId);
		if (csvData == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found or expired.".getBytes());
		}
		return connectionUtils.buildCsvResponse(csvData, "employees.csv");
	}

	@DeleteMapping("/{fileId}")
	public ResponseEntity<String> deleteEmployeeData(@PathVariable String fileId) {
		service.deleteCsvFromTempStorage(fileId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted: " + fileId);
	}

	@PostMapping(value = "/{quantity}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<byte[]> dataGeneration(@PathVariable int quantity) {
		byte[] csvData = service.generateEmployeeData(quantity);
		return connectionUtils.buildCsvResponse(csvData, "employees.csv");
	}

	@PostMapping(value = "/batch/small", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<byte[]> dataSmallGeneration() {
		final int quantity = 100;
		byte[] csvData = service.generateEmployeeData(quantity);
		return connectionUtils.buildCsvResponse(csvData, "employees.csv");
	}

	@PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> insertAtributes(@RequestBody @Valid EmployeeRequest request) {
		utils.setVariables(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Inserted Values");
	}

}
