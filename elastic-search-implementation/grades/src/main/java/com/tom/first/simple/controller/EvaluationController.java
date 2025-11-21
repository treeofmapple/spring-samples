package com.tom.first.simple.controller;

import java.util.List;

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

import com.tom.first.simple.dto.EvaluationRequest;
import com.tom.first.simple.dto.EvaluationResponse;
import com.tom.first.simple.service.EvaluationService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

	private final EvaluationService service;

	@GetMapping(params = "id")
	public ResponseEntity<EvaluationResponse> findById(@RequestParam long query) {
		var response = service.findById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "subject")
	public ResponseEntity<EvaluationResponse> findBySubject(@RequestParam String query) {
		var response = service.findBySubject(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "grade")
	public ResponseEntity<List<EvaluationResponse>> findByGrade(@RequestParam double grade) {
		var response = service.findByGrade(grade);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/data/start")
	public ResponseEntity<Void> startDataStreaming(@RequestParam(required = false, defaultValue = "200") int speed) {
		service.startStreaming(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/data/stop")
	public ResponseEntity<Void> stopDataStreaming() {
		service.stopStreaming();
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping
	public ResponseEntity<EvaluationResponse> createEvaluation(
			@RequestBody(required = true) EvaluationRequest request) {
		var response = service.createEvaluation(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping(value = "/{query}")
	public ResponseEntity<Void> deleteEvaluation(@PathVariable long query) {
		service.deleteEvaluationById(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping(params = "subject")
	public ResponseEntity<Void> deleteEvaluation(@RequestParam String subject) {
		service.deleteEvaluationBySubject(subject);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
