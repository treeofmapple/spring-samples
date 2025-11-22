package com.tom.first.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.library.dto.BookRequest;
import com.tom.first.library.dto.BookResponse;
import com.tom.first.library.service.BookService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/book")
@RequiredArgsConstructor
public class BookController {

	private final BookService service;

	@GetMapping(params = "id")
	public ResponseEntity<BookResponse> findById(@RequestParam long query) {
		var response = service.findById(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "subject")
	public ResponseEntity<BookResponse> findByTitle(@RequestParam String query) {
		var response = service.findByTitle(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping(params = "author")
	public ResponseEntity<List<BookResponse>> findAllBooksFromAuthor(@RequestParam String query) {
		var response = service.findAllBooksFromAuthor(query);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<BookResponse> createBook(@RequestBody(required = true) BookRequest request) {
		var response = service.createBook(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping(params = "title")
	public ResponseEntity<Void> deleteBookByTitle(@RequestParam(value = "title") String query) {
		service.deleteBookByTitle(query);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
