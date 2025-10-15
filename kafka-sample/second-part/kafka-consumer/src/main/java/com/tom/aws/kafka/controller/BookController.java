package com.tom.aws.kafka.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.aws.kafka.book.BookPageResponse;
import com.tom.aws.kafka.book.BookResponse;
import com.tom.aws.kafka.book.BookService;
import com.tom.aws.kafka.book.UpdateRequest;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService service;

	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookPageResponse> findBook(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String isbn, 
			@RequestParam(required = false) String title, 
			@RequestParam(required = false) String author,
			@RequestParam(required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		var response = service.searchBookByParams(page, isbn, title, author, startDate, endDate);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(value = "/search/id", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookResponse> findBookById(
			@RequestParam long query
			) {
		var response = service.searchBookById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping(value = "/update/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookResponse> updateBook(
			@PathVariable("id") long query, 
			UpdateRequest request) {
		var response = service.updateBook(query, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping(value = "/remove/{id}")
	public ResponseEntity<Void> deleteBook(
			@PathVariable("id") long query) {
		service.deleteBook(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
