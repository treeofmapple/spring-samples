package com.tom.benchmark.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.benchmark.order.dto.order.OrderResponse;
import com.tom.benchmark.order.dto.orderitem.OrderItemRequest;
import com.tom.benchmark.order.service.OrderItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/order/{orderId}/item")
@RequiredArgsConstructor
public class OrderItemController {

	private final OrderItemService service;
	
	@PostMapping( 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrderResponse> addItemToOrder(@PathVariable Long orderId, @RequestBody @Valid OrderItemRequest request) {
		var response = service.addItemToOrder(orderId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// removeItemFromOrder
	
}
