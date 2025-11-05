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

import com.tom.first.simple.dto.EvaluationRequest;
import com.tom.first.simple.dto.EvaluationResponse;
import com.tom.first.simple.dto.evaluation.EvaluationUpdate;
import com.tom.first.simple.dto.evaluation.GradeRequest;
import com.tom.first.simple.dto.evaluation.SubjectRequest;
import com.tom.first.simple.dto.user.NameRequest;
import com.tom.first.simple.service.EvaluationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

	private final EvaluationService service;

	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EvaluationResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}

	@GetMapping(value = "/get/subject")
	public ResponseEntity<EvaluationResponse> findBySubject(@RequestParam SubjectRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findBySubject(request));
	}

	@GetMapping(value = "/get/grade")
	public ResponseEntity<List<EvaluationResponse>> findByGrade(@RequestParam GradeRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findByGrade(request));
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EvaluationResponse> createEvaluation(@RequestBody @Valid EvaluationRequest request) {
		var response = service.createEvaluation(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EvaluationResponse> updateEvaluation(@RequestParam SubjectRequest nameRequest,
			@RequestBody @Valid EvaluationUpdate request) {
		var response = service.updateEvaluation(nameRequest, request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@DeleteMapping(value = "/delete")
	public ResponseEntity<String> deleteEvaluation(@RequestParam SubjectRequest request) {
		service.deleteEvaluation(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("Evaluation Deleted: %s", request));
	}

	@DeleteMapping(value = "/delete/evaluation")
	public ResponseEntity<String> deleteAllUsersFromEvaluation(@RequestParam SubjectRequest request) {
		service.deleteAllUsersFromEvaluation(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(String.format("Users from Evaluation deleted: %s", request));
	}

	@DeleteMapping(value = "/delete/user")
	public ResponseEntity<String> deleteAllEvaluationFromUser(@RequestParam NameRequest request) {
		service.deleteAllEvaluationFromUser(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT)
				.body(String.format("Deleting all evaluation from user: %s", request));
	}

}
