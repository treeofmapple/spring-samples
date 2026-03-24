package com.tom.front.basic.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.front.basic.dto.BookPageResponse;
import com.tom.front.basic.dto.BookRequest;
import com.tom.front.basic.dto.BookResponse;
import com.tom.front.basic.dto.UpdateRequest;
import com.tom.front.basic.service.BookService;

import jakarta.servlet.http.HttpServletRequest;
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
			@RequestParam(required = false) String title, 
			@RequestParam(required = false) String author,
			@RequestParam(required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			HttpServletRequest httpRequest) {
		var response = service.findBookByParams(page, title, author, startDate, endDate, httpRequest);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookResponse> createBook(
			@RequestBody BookRequest request, 
			HttpServletRequest httpRequest) {
		var response = service.createBook(request, httpRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping(value = "/{id}/isbn", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookResponse> updateIsbn(
			@PathVariable long id, 
			HttpServletRequest httpRequest) {
		var response = service.updateIsbn(id, httpRequest);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BookResponse> updateBook(
			@PathVariable long id, 
			UpdateRequest request,
			HttpServletRequest httpRequest) {
		var response = service.editBook(id, request, httpRequest);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping(value = "/remove")
	public ResponseEntity<Void> deleteBook(
			@PathVariable long id, 
			HttpServletRequest httpRequest) {
		service.deleteBook(id, httpRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
