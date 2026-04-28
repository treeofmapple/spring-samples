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

import br.tekk.system.library.request.BookItemRequest;
import br.tekk.system.library.request.BookItemResponse;
import br.tekk.system.library.service.BookItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class BookItemController {

	private final BookItemService bookItemService;

	@Operation(summary = "Get all book items", description = "Retrieves a list of all book items.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of book items retrieved successfully"),
			@ApiResponse(responseCode = "404", description = "No book items found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get")
	public ResponseEntity<List<BookItemResponse>> findAll() {
		return ResponseEntity.ok(bookItemService.findAllBookItems());
	}

	@Operation(summary = "Get book item by ID", description = "Retrieves a book item by its unique ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Book item found"),
			@ApiResponse(responseCode = "404", description = "Book item not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@GetMapping("/get/{id}")
	public ResponseEntity<BookItemResponse> findById(@PathVariable Integer id) {
		return ResponseEntity.ok(bookItemService.findById(id));
	}

	@Operation(summary = "Create a new book item", description = "Creates a new book item and returns its ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Book item created successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "406", description = "User has rented maximum allowed copies"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/create")
	public ResponseEntity<Integer> createBookItem(@RequestBody BookItemRequest request) {
		return ResponseEntity.status(201).body(bookItemService.createBookItem(request));
	}

	@Operation(summary = "Update book item", description = "Updates an existing book item by ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Book item updated successfully"),
			@ApiResponse(responseCode = "404", description = "Book item not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PutMapping("/update/{id}")
	public ResponseEntity<Void> updateBookItem(@PathVariable Integer id, @RequestBody BookItemRequest request) {
		bookItemService.updateBookItem(id, request);
		return ResponseEntity.accepted().build();
	}

	@Operation(summary = "Delete book item", description = "Deletes a book item by its unique ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Book item deleted successfully"),
			@ApiResponse(responseCode = "404", description = "Book item not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBookItem(@PathVariable Integer id) {
		bookItemService.deleteBookItem(id);
		return ResponseEntity.accepted().build();
	}

	/*
	 * @Operation(summary = "Sell book item", description =
	 * "Changes the status of a book item to sold.")
	 * 
	 * @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
	 * "Book item sold successfully"),
	 * 
	 * @ApiResponse(responseCode = "404", description = "Book item not found"),
	 * 
	 * @ApiResponse(responseCode = "500", description = "Internal server error") })
	 * 
	 * @PutMapping("/sell/{id}") public ResponseEntity<Void>
	 * sellBookItem(@PathVariable Integer id) { bookItemService.sellBookItem(id);
	 * return ResponseEntity.ok().build(); }
	 * 
	 * @Operation(summary = "Start renting a book item", description =
	 * "Sets the status of a book item to rented.")
	 * 
	 * @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
	 * "Renting started successfully"),
	 * 
	 * @ApiResponse(responseCode = "404", description = "Book item not found"),
	 * 
	 * @ApiResponse(responseCode = "500", description = "Internal server error") })
	 * 
	 * @PutMapping("/rent/start/{id}") public ResponseEntity<LocalDate>
	 * startRentBook(@PathVariable Integer id, BookItemRequest request) { LocalDate
	 * rentStart = bookItemService.startRentBook(id, request); return
	 * ResponseEntity.ok(rentStart); }
	 * 
	 * @Operation(summary = "Return a rented book item", description =
	 * "Sets the status of a book item to returned.")
	 * 
	 * @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
	 * "Book item returned successfully"),
	 * 
	 * @ApiResponse(responseCode = "404", description = "Book item not found"),
	 * 
	 * @ApiResponse(responseCode = "500", description = "Internal server error") })
	 * 
	 * @PutMapping("/return/{id}") public ResponseEntity<LocalDate>
	 * returnBookItem(@PathVariable Integer id, BookItemRequest request) { LocalDate
	 * rentEnd = bookItemService.returnBookItem(id, request); return
	 * ResponseEntity.ok(rentEnd); }
	 */
}
