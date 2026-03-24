package com.tom.first.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.library.service.BookStreamingService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/book/streaming")
@RequiredArgsConstructor
public class BookStreamingController {

	private final BookStreamingService dataStreaming;

	@PostMapping(value = "/start")
	public ResponseEntity<Void> startDataStreaming(@RequestParam(required = false, defaultValue = "200") int speed) {
		dataStreaming.startCreatingBook(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/stop")
	public ResponseEntity<Void> stopDataStreaming() {
		dataStreaming.stopCreatingBook();
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/author/start")
	public ResponseEntity<Void> startOneAuthorDataStreaming(@RequestParam(required = false, defaultValue = "200") int speed) {
		dataStreaming.startCreatingTenBooksOneAuthor(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/author/stop")
	public ResponseEntity<Void> stopOneAuthorDataStreaming() {
		dataStreaming.stopCreatingTenBooksBook();
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
