package com.tom.first.library.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.tom.first.library.dto.ItemRequest;
import com.tom.first.library.dto.ItemResponse;
import com.tom.first.library.dto.ItemResponse.ItemBookResponse;
import com.tom.first.library.dto.UserRequest.NameRequest;
import com.tom.first.library.service.ItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

	private final ItemService service;
	
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ItemResponse>> findAll() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
	}
	
	@GetMapping(value = "/get/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ItemBookResponse>> findItemByUser(@RequestBody @Valid NameRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findItemByUser(request));
	}
	
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemBookResponse> createItem(@RequestBody @Valid ItemRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.createItem(request));
	}
	
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemBookResponse> updateItem(@RequestBody @Valid ItemRequest request) {
		service.updateItem(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PutMapping(value = "/rent/start", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ItemBookResponse> startItemRent(@RequestBody @Valid ItemRequest request) {
		service.startRent(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@DeleteMapping(value = "/delete", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<Void> deleteItem(@RequestBody @Valid ItemRequest request) {
		service.deleteItem(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
