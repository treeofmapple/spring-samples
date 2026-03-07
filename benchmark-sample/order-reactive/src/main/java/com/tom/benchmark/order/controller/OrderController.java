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

import com.tom.benchmark.order.dto.order.OrderRequest;
import com.tom.benchmark.order.dto.order.OrderResponse;
import com.tom.benchmark.order.dto.order.OrderUpdate;
import com.tom.benchmark.order.dto.order.PageOrderResponse;
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
	public Mono<OrderResponse> searchOrderById(@PathVariable(value = "id") UUID orderId) {
		return service.searchOrderById(orderId);
	}

	@GetMapping(value = "/search")
	@ResponseStatus(HttpStatus.OK)
	public Mono<PageOrderResponse> searchClientByParams(
			@RequestParam(defaultValue = "0", required = false) @Min(0) int page) {
		return service.searchOrderByParams(page);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<OrderResponse> createOrder(@RequestBody OrderRequest request) {
		return service.createOrder(request);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public Mono<OrderResponse> updateOrder(@RequestBody OrderUpdate request) {
		return service.updateOrder(request);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteOrderById(@PathVariable(value = "id") UUID orderId) {
		return service.deleteOrderById(orderId);
	}

}
