package com.tom.benchmark.order.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.benchmark.client.dto.ClientUpdate;
import com.tom.benchmark.client.dto.PageClientResponse;
import com.tom.benchmark.order.dto.order.OrderResponse;
import com.tom.benchmark.order.logic.client.ClientRequest;
import com.tom.benchmark.order.logic.client.ClientResponse;
import com.tom.benchmark.order.service.OrderService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService service;

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Mono<OrderResponse> searchClientById(@PathVariable(value = "id") UUID userId) {
		return service.searchClientById(userId);
	}

	@GetMapping(value = "/search")
	@ResponseStatus(HttpStatus.OK)
	public Mono<PageClientResponse> searchClientByParams(
			@RequestParam(defaultValue = "0", required = false) @Min(0) int page,
			@RequestParam(required = false) String name, @RequestParam(required = false) String email) {
		return service.searchClientByParams(page, name, email);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<ClientResponse> createClient(@RequestBody ClientRequest request) {
		return service.createClient(request);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<ClientResponse> updateClient(@RequestBody ClientUpdate request) {
		return service.updateClient(request);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteClient(@PathVariable(value = "id") UUID userId) {
		return service.deleteClient(userId);
	}
	
}
