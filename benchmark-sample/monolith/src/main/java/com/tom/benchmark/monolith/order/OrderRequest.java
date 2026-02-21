package com.tom.benchmark.monolith.order;

import java.util.List;

import com.tom.benchmark.monolith.order_items.OrderItemRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
		
	    @NotNull(message = "Client cpf cannot be null")
	    String clientCpf,

	    @NotEmpty(message = "Order must contain at least one item")
	    List<OrderItemRequest> items
		
		) {

}
