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
import com.tom.service.datagen.dto.RandomRequest;
import com.tom.service.datagen.service.SchoolService;
import com.tom.service.datagen.service.SchoolUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/school")
@RequiredArgsConstructor
public class SchoolController {

	private final ConnectionUtils connectionUtils;
	private final SchoolService service;
	private final SchoolUtils utils;
	
	@GetMapping(value = "/{fileId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<byte[]> downloadSchoolData(@PathVariable String fileId) {
		byte[] csvData = service.retrieveFromTempStorage(fileId);
		
		if (csvData == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found or expired.".getBytes());
		}
		return connectionUtils.buildCsvResponse(csvData, "school.csv");
	}
	
	@PostMapping(value = "/{quantity}")
	public ResponseEntity<byte[]> generateSchoolData(@PathVariable Integer quantity) {
		byte[] csvData = service.generateSchoolData();
		return connectionUtils.buildCsvResponse(csvData, "school.csv");
	}
	
	@PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> insertAtributes(@RequestBody @Valid RandomRequest request) {
		utils.setVariables(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Inserted Values");
	}
	
	@DeleteMapping("/{fileId}")
	public ResponseEntity<String> deleteEmployeeData(@PathVariable String fileId) {
		service.retrieveFromTempStorage(fileId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted: " + fileId);
	}
}
