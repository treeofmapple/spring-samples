package com.tom.benchmark.order.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tom.benchmark.order.dto.orderitem.OrderItemRequest;
import com.tom.benchmark.order.dto.orderitem.OrderItemResponse;
import com.tom.benchmark.order.service.OrderItemService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/v1/order/{orderId}/item")
@RequiredArgsConstructor
public class OrderItemController {

	private final OrderItemService service;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<OrderItemResponse> addItemToOrder(@PathVariable UUID orderId, @RequestBody OrderItemRequest request) {
		return service.addItemToOrder(orderId, request);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> removeItemFromOrder(@PathVariable UUID orderId, @PathVariable(value = "id") UUID orderItemId) {
		return service.removeItemFromOrder(orderId, orderItemId);
	}

}
