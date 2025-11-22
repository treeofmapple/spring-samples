package com.tom.first.simple.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.simple.service.EvaluationStreamingService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/book/streaming")
@RequiredArgsConstructor
public class EvaluationStreamingController {

	private final EvaluationStreamingService dataStreaming;

	@PostMapping(value = "/start")
	public ResponseEntity<Void> startDataStreaming(@RequestParam(required = false, defaultValue = "200") int speed) {
		dataStreaming.startCreatingEvaluation(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/stop")
	public ResponseEntity<Void> stopDataStreaming() {
		dataStreaming.stopCreatingEvaluation();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
