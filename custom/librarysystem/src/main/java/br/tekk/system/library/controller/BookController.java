package br.tekk.system.library.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.tekk.system.library.request.BookRequest;
import br.tekk.system.library.request.BookResponse;
import br.tekk.system.library.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

	private final BookService bookService;

	@Operation(summary = "Get all books", description = "Retrieves a list of all books.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of books retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "No books found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get")
	public ResponseEntity<List<BookResponse>> findAll() {
		return ResponseEntity.ok(bookService.findAllBooks());
	}

	@Operation(summary = "Get book by ID", description = "Retrieves a book by its unique ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Book found"),
			@ApiResponse(responseCode = "404", description = "Book not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/{id}")
	public ResponseEntity<BookResponse> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(bookService.findById(id));
	}

	@Operation(summary = "Create a new book", description = "Creates a new book and returns the book ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Book created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "406", description = "Book already exists"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/create")
	public ResponseEntity<Integer> createBook(@RequestBody BookRequest request) {
		return ResponseEntity.status(201).body(bookService.createBook(request));
	}

	@Operation(summary = "Update book", description = "Updates an existing book by ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Book updated successfully"),
			@ApiResponse(responseCode = "404", description = "Book not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateBook(@PathVariable Integer id, @RequestBody BookRequest request) {
		bookService.updateBook(id, request);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Delete book", description = "Deletes a book by its unique ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Book not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
		bookService.deleteBook(id);
		return ResponseEntity.noContent().build();
	}
}
