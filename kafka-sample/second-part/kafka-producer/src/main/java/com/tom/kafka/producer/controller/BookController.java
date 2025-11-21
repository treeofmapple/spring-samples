package com.tom.kafka.producer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.kafka.producer.book.BookResponse;
import com.tom.kafka.producer.book.BookService;
import com.tom.kafka.producer.produce.BookStreamProducer;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService service;
	private final BookStreamProducer bookStream;

	@PostMapping(value = "/send")
	public ResponseEntity<BookResponse> sendRandomBookToAws() {
		var response = service.sendBookRandomToKafka();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/generate/{quantity}")
	public ResponseEntity<Void> generateSomeBooks(@PathVariable int quantity) {
		service.generateBooks(quantity);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PostMapping(value = "/stream/start")
	public ResponseEntity<Void> startDataStreaming(@RequestParam(required = false, defaultValue = "100") int speed) {
		bookStream.startStreaming(speed);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping(value = "/stream/stop")
	public ResponseEntity<Void> stopDataStreaming() {
		bookStream.stopStreaming();
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
