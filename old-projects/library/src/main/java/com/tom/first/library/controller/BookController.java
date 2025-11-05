package com.tom.first.library.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.BookRequest.AuthorRequest;
import com.tom.first.library.dto.BookRequest.TitleRequest;
import com.tom.first.library.dto.BookResponse;
import com.tom.first.library.dto.BookResponse.BookUpdateResponse;
import com.tom.first.library.service.BookService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

	private final BookService service;
	
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@GetMapping(value = "/get/title", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookResponse> findByTitle(@RequestParam @Valid TitleRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findByTitle(request));
	}
	
	@GetMapping(value = "/get/author", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookResponse>> findByAuthor(@RequestParam @Valid AuthorRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findByAuthor(request));
	}
	
	@GetMapping(value = "/get/year", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookResponse>> findByRangeLaunchYear(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findByRangeLaunchYear(startDate, endDate));
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createBook(request));
	}
	
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookUpdateResponse> updateBook(@RequestParam @Valid TitleRequest title, @RequestBody @Valid BookRequest request) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.updateBook(title, request));
	}
	
	@DeleteMapping(value = "/delete", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> deleteBookByTitle(@RequestParam @Valid TitleRequest request) {
		service.deleteBookByTitle(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(String.format("Deleting book: %s", request.title()));
	}
	
}
