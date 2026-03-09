package com.tom.benchmark.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.benchmark.order.dto.OrderRequest;
import com.tom.benchmark.order.dto.OrderResponse;
import com.tom.benchmark.order.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService service;
	
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderResponse>> findAll() {
		var response = service.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
		var response = service.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/client/cpf", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderResponse>> findByClientCpf(@RequestParam String cpf) {
		var response = service.findByClientCpf(cpf);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping(value = "/create", 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
		var response = service.createOrder(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Void> deleteOrderByClientCpf(@RequestParam String cpf) {
		service.deleteOrderByClientCpf(cpf);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
